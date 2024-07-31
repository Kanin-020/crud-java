package org.example.system.dao;

import org.example.system.model.Person;
import org.example.system.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

    public void createPerson(Person person) {

        try {
            String query = "INSERT INTO persons (name, age, height) VALUES (?, ?, ?)";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setFloat(3, person.getHeight());

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Person getPerson(int id) {

        try {

            String query = "SELECT * FROM persons WHERE id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setHeight(resultSet.getFloat("height"));

                return person;
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public List<Person> getPersonList() {

        List<Person> personList = new ArrayList<>();

        String query = "SELECT * FROM persons";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Person person = new Person();

                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setHeight(resultSet.getFloat("height"));

                if (person != null) {
                    personList.add(person);
                }

            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return personList;

    }

    public boolean updatePerson(Person person) {

        try {

            String query = "UPDATE persons SET name = ?, age = ?, height = ? WHERE id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setFloat(3, person.getHeight());
            preparedStatement.setFloat(4, person.getId());

            preparedStatement.executeUpdate();

            return true;

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }

    }

    public void deletePerson(int id) {

        try {

            String query = "DELETE FROM persons WHERE id = ?";

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
