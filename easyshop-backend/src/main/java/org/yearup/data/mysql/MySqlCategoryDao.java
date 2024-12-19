package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();
        // get all categories
        String sql = """
                SELECT * FROM categories
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                categories.add(new Category(categoryId, name, description));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        String sql = """
                SELECT * FROM categories
                WHERE category_id = ?
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            ResultSet row = statement.executeQuery();

            if (row.next())
            {
                return mapRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        String sql = """
                INSERT INTO categories (name, description)
                VALUES (?, ?)
                """;
        int generatedId = -1;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            statement.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    generatedId = keys.getInt(1);
                    category.setCategoryId(generatedId); // Set the ID in the Category object
                }
            }
           // Category newCategory = getById(category.getCategoryId());
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        String sql = """
                UPDATE categories
                SET name = ?, description = ?
                WHERE category_id = ?
                """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            int rows = statement.executeUpdate();
            System.out.println(rows + " rows updated...");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
