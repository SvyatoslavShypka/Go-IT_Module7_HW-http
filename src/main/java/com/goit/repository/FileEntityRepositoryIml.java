package com.goit.repository;

import com.goit.datasource.Datasource;
import com.goit.entity.FileEntity;
import com.goit.exception.DatasourceException;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//@Slf4j
//DAO - DATA ACCESS OBJECT
public class FileEntityRepositoryIml extends JDBCRepository<FileEntity> {

    private static final String TABLE_NAME = "app_file";
    private static final Logger LOG = LoggerFactory.getLogger(FileEntityRepositoryIml.class);

    @Language("SQL")
    private static final String INSERT_FILE = """
            INSERT INTO app_file (id, name, extension, creation_date) VALUES (?, ?, ?, ?)
            """;

    @Language("SQL")
    private static final String UPDATE_FILE = """
            UPDATE app_file SET name=?, extension=?, creation_date=? WHERE id=?;
            """;

    public FileEntityRepositoryIml(Datasource datasource) {
        super(datasource);
        LOG.info("Created FileEntityRepositoryIml");
    }

    @Override
    public List<FileEntity> findAll() {
        LOG.info("loading all files");
        try {
            PreparedStatement preparedStatement = datasource.preparedStatement(getFindAllQuery(TABLE_NAME), true);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<FileEntity> customerEntities = new ArrayList<>();
            while (resultSet.next()) {
                FileEntity fileEntity = parseRow(resultSet);
                customerEntities.add(fileEntity);
            }
            datasource.close();
            LOG.info("all files loaded");
            return customerEntities;
        } catch (Exception e) {
            String message = "findAll";
            LOG.error(message, e);
            throw new DatasourceException(message, e);
        }
    }

    @Override
    public FileEntity findById(String id) {
        try {
            PreparedStatement preparedStatement = datasource.preparedStatement(getFindByIdQuery(TABLE_NAME), true);
//            preparedStatement.setString(1, "id");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean next = resultSet.next();
            if (!next) {
                return null;
            }
            FileEntity fileEntity = parseRow(resultSet);
            datasource.close();
            return fileEntity;
        } catch (Exception e) {
            String message = "findById";
            LOG.error(message, e);
            throw new DatasourceException(message, e);
        }
    }

    @Override
    public FileEntity save(FileEntity entity) {
        try {
            FileEntity byId = findById(entity.getId());
            if (Objects.isNull(byId)) {
                PreparedStatement preparedStatement = datasource.preparedStatement(INSERT_FILE, true);
                final int idIndex = 1;
                final int nameIndex = 2;
                final int contactNameIndex = 3;
                final int countryIndex = 4;

                String id = UUID.randomUUID().toString();
                entity.setId(id);
                preparedStatement.setString(idIndex, id);
                preparedStatement.setString(nameIndex, entity.getName());
                preparedStatement.setString(contactNameIndex, entity.getExtension());
                preparedStatement.setLong(countryIndex, entity.getCreationDate());
                preparedStatement.executeUpdate();
                datasource.close();
                return entity;
            } else {
                return update(entity);
            }
        } catch (Exception e) {
            String message = "save";
            LOG.error(message, e);
            throw new DatasourceException(message, e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            PreparedStatement preparedStatement = datasource.preparedStatement(getDeleteByIdQuery(TABLE_NAME));
            final int fileIdIndex = 1;
            final int idIndex = 2;
            preparedStatement.setString(fileIdIndex, "id");
            preparedStatement.setString(idIndex, id);
            preparedStatement.executeUpdate();
            datasource.close();
        } catch (Exception e) {
            String message = "delete";
            LOG.error(message, e);
            throw new DatasourceException(message, e);
        }
    }

    private FileEntity update(FileEntity entity) {
        try {
            PreparedStatement preparedStatement = datasource.preparedStatement(UPDATE_FILE, true);
            final int nameIndex = 1;
            final int extensionIndex = 2;
            final int creationDateIndex = 3;
            final int idIndex = 4;

            preparedStatement.setString(nameIndex, entity.getName());
            preparedStatement.setString(extensionIndex, entity.getExtension());
            preparedStatement.setLong(creationDateIndex, entity.getCreationDate());
            preparedStatement.setString(idIndex, entity.getId());
            preparedStatement.executeUpdate();
            datasource.close();
            return entity;
        } catch (Exception e) {
            String message = "update";
            LOG.error(message, e);
            throw new DatasourceException(message, e);
        }
    }

    private FileEntity parseRow(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String extension = resultSet.getString("extension");
        Long creationDate = resultSet.getLong("creation_date");
        return FileEntity.of(id, name, extension, creationDate);
    }
}
