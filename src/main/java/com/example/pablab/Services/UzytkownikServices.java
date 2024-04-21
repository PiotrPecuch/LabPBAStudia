package com.example.pablab.Services;

import com.example.pablab.Config.HibernateUtil;
import com.example.pablab.Entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UzytkownikServices {

    DialogsServices dialogsServices = new DialogsServices();

    @Getter
    public ObservableList<Map<String, Object>> usersData = FXCollections.observableArrayList();

    public void fetchUserData() {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root);

            Query<User> query = session.createQuery(criteriaQuery);
            List<User> userList = query.list();

            usersData.clear();
            for (User user : userList) {
                Map<String, Object> row = new HashMap<>();
                row.put("user_id", user.getUserId());
                row.put("first_name", user.getFirstName());
                row.put("last_name", user.getLastName());
                row.put("birth_date", user.getBirthDate());
                row.put("user_email", user.getUserEmail());
                row.put("user_phone_number", user.getUserPhoneNumber());
                usersData.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

        int length = 12;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }

    public List<Map<String, Object>> findUserDynamically(String imie, String nazwisko) {
        try (Session session = HibernateUtil.getSession()) {
            StringBuilder hql = new StringBuilder("SELECT userId, firstName, lastName, birthDate, userEmail, userPhoneNumber FROM User WHERE 1=1");

            List<String> conditions = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>();

            if (imie != null && !imie.isEmpty()) {
                conditions.add("firstName LIKE :imie");
                parameters.put("imie", "%" + imie + "%");
            }

            if (nazwisko != null && !nazwisko.isEmpty()) {
                conditions.add("lastName LIKE :nazwisko");
                parameters.put("nazwisko", "%" + nazwisko + "%");
            }

            if (!conditions.isEmpty()) {
                hql.append(" AND ").append(String.join(" AND ", conditions));
            }

            Query<Object[]> query = session.createQuery(hql.toString(), Object[].class);

            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            List<Object[]> resultList = query.getResultList();

            List<Map<String, Object>> users = new ArrayList<>();
            for (Object[] result : resultList) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("user_id", result[0]);
                userMap.put("first_name", result[1]);
                userMap.put("last_name", result[2]);
                userMap.put("birth_date", result[3]);
                userMap.put("user_email", result[4]);
                userMap.put("user_phone_number", result[5]);
                users.add(userMap);
            }

            return users;
        }
    }

    public User findUserById(Integer id) {
        ObservableList<Map<String, Object>> usersData = FXCollections.observableArrayList();
        try (Session session = HibernateUtil.getSession()) {
            User user = session.get(User.class, id);
            return user;
        }
    }

    public void updateUserById(Long userId, String name, String lastName, LocalDate birthDate, String email, String phoneNumber) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, userId);
            if (user != null) {
                user.setFirstName(name);
                user.setLastName(lastName);
                user.setBirthDate(birthDate);
                user.setUserEmail(email);
                user.setUserPhoneNumber(phoneNumber);

                session.update(user);
                transaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String deleteUser(Long id) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                User user = session.get(User.class, id);
                if (user == null) {
                    return "Użytkownik o ID " + id + " nie istnieje";
                }
                if (dialogsServices.showConfirmationDialog("Czy chcesz usunąć użytkownika o ID: " + id + "?")) {
                    session.delete(user);
                    transaction.commit();
                    return "Usunięto";
                } else {
                    transaction.rollback();
                    return "Anulowano";
                }
            } catch (Exception e) {
                return "Wystąpił błąd";
            }
        }
    }

    public String addUser(User user, Button button) {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(user);
                transaction.commit();

                Stage stage = (Stage) button.getScene().getWindow();
                stage.close();

                return "Dodano";
            } catch (Exception e) {
                assert transaction != null;
                transaction.rollback();
                e.printStackTrace();
                return "Dodawanie nie powiodło się";
            }
        }
    }
}
