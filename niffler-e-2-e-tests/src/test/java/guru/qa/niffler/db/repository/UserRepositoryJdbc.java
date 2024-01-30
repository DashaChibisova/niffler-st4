package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.db.model.CurrencyValues;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;
import guru.qa.niffler.db.model.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

  private final DataSource authDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.AUTH);
  private final DataSource udDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA);

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement userPs = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
           PreparedStatement authorityPs = conn.prepareStatement(
               "INSERT INTO \"authority\" " +
                   "(user_id, authority) " +
                   "VALUES (?, ?)")
      ) {

        userPs.setString(1, user.getUsername());
        userPs.setString(2, pe.encode(user.getPassword()));
        userPs.setBoolean(3, user.getEnabled());
        userPs.setBoolean(4, user.getAccountNonExpired());
        userPs.setBoolean(5, user.getAccountNonLocked());
        userPs.setBoolean(6, user.getCredentialsNonExpired());

        userPs.executeUpdate();

        UUID authUserId;
        try (ResultSet keys = userPs.getGeneratedKeys()) {
          if (keys.next()) {
            authUserId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }

        for (Authority authority : Authority.values()) {
          authorityPs.setObject(1, authUserId);
          authorityPs.setString(2, authority.name());
          authorityPs.addBatch();
          authorityPs.clearParameters();
        }

        authorityPs.executeBatch();
        conn.commit();
        user.setId(authUserId);
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
    try (Connection conn = authDs.getConnection();
         PreparedStatement usersPs = conn.prepareStatement("SELECT * " +
             "FROM \"user\" u " +
             "JOIN \"authority\" a ON u.id = a.user_id " +
             "where u.id = ?")) {
      usersPs.setObject(1, id);

      usersPs.execute();
      UserAuthEntity user = new UserAuthEntity();
      boolean userProcessed = false;
      try (ResultSet resultSet = usersPs.getResultSet()) {
        while (resultSet.next()) {
          if (!userProcessed) {
            user.setId(resultSet.getObject(1, UUID.class));
            user.setUsername(resultSet.getString(2));
            user.setPassword(resultSet.getString(3));
            user.setEnabled(resultSet.getBoolean(4));
            user.setAccountNonExpired(resultSet.getBoolean(5));
            user.setAccountNonLocked(resultSet.getBoolean(6));
            user.setCredentialsNonExpired(resultSet.getBoolean(7));
            userProcessed = true;
          }

          AuthorityEntity authority = new AuthorityEntity();
          authority.setId(resultSet.getObject(8, UUID.class));
          authority.setAuthority(Authority.valueOf(resultSet.getString(10)));
          user.getAuthorities().add(authority);
        }
      }
      return userProcessed ? Optional.of(user) : Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, currency) " +
              "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getCurrency().name());
        ps.executeUpdate();

        UUID userId;
        try (ResultSet keys = ps.getGeneratedKeys()) {
          if (keys.next()) {
            userId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }
        user.setId(userId);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

    @Override
    public void deleteInAuthById(UUID id) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement userPs = conn.prepareStatement(
                    "DELETE FROM \"user\" WHERE id = ?");

                 PreparedStatement authorityPs = conn.prepareStatement(
                         "DELETE FROM \"authority\" where user_id = ?")

            ) {

                authorityPs.setObject(1, id);
                authorityPs.executeUpdate();
                userPs.setObject(1, id);
                userPs.executeUpdate();
                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteInUserDataById(UUID id) {
        try (Connection conn = udDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM \"user\" WHERE id = ?")) {
                ps.setObject(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInAuthById(UserAuthEntity userAuth) {
        try (Connection conn = authDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE \"user\" " +
                            "SET password = ?, enabled = ?, account_non_expired = ?, " +
                            "account_non_locked = ?, credentials_non_expired = ? WHERE id = ?")) {
                ps.setObject(1, pe.encode(userAuth.getPassword()));
                ps.setBoolean(2, userAuth.getEnabled());
                ps.setBoolean(3, userAuth.getAccountNonExpired());
                ps.setBoolean(4, userAuth.getAccountNonLocked());
                ps.setBoolean(5, userAuth.getCredentialsNonExpired());
                ps.setObject(6, userAuth.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateInUserDataById(UUID idUser, String userName, CurrencyValues currency) {
        try (Connection conn = udDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE \"user\" " +
                            "SET username = ?, currency = ? " +
                            "WHERE id = ?")) {
                ps.setObject(1, userName);
                ps.setObject(2, currency);
                ps.setObject(3, idUser);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity getUserDataByName(String userName) {
        UserEntity userEntity = new UserEntity();
  @Override
  public Optional<UserEntity> findByIdInUserdata(UUID id) {
    UserEntity user = new UserEntity();
    try (Connection conn = udDs.getConnection();
         PreparedStatement usersPs = conn.prepareStatement("SELECT * FROM \"user\" WHERE id = ? ")) {
      usersPs.setObject(1, id);
      usersPs.execute();
      try (ResultSet resultSet = usersPs.getResultSet()) {
        if (resultSet.next()) {
          user.setId(resultSet.getObject("id", UUID.class));
          user.setUsername(resultSet.getString("username"));
          user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
          user.setFirstname(resultSet.getString("firstname"));
          user.setSurname(resultSet.getString("surname"));
          user.setPhoto(resultSet.getBytes("photo"));
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return Optional.of(user);
  }

  @Override
  public void deleteInAuthById(UUID id) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement usersPs = conn.prepareStatement("DELETE FROM \"user\" WHERE id = ?");
           PreparedStatement authorityPs = conn.prepareStatement("DELETE FROM \"authority\" WHERE user_id = ?")) {

        try (Connection conn = udDs.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM \"user\" WHERE username= ?")) {

                ps.setObject(1, userName);

                try (ResultSet result = ps.executeQuery()) {
                    while (result.next()) {
                        userEntity.setId(UUID.fromString(result.getString("id")));
                        userEntity.setUsername(result.getString("username"));
                        userEntity.setCurrency(CurrencyValues.valueOf(result.getString("currency")));
                        userEntity.setFirstname(result.getString("firstname"));
                        userEntity.setSurname(result.getString("surname"));
                        userEntity.setPhoto(result.getBytes("photo"));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntity;
    }
        authorityPs.setObject(1, id);
        usersPs.setObject(1, id);
        authorityPs.executeUpdate();
        usersPs.executeUpdate();

        conn.commit();
      } catch (SQLException e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

    @Override
    public UserAuthEntity getUserAuthByName(String userName) {
        UserAuthEntity userAuthEntity = new UserAuthEntity();

        try (Connection conn = authDs.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM \"user\" WHERE username= ?")) {

                ps.setObject(1, userName);
  @Override
  public void deleteInUserdataById(UUID id) {
    try (Connection conn = udDs.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement usersPs = conn.prepareStatement("DELETE FROM \"user\" WHERE id = ?");
           PreparedStatement friendsPs = conn.prepareStatement("DELETE FROM friendship WHERE user_id = ?");
           PreparedStatement invitesPs = conn.prepareStatement("DELETE FROM friendship WHERE friend_id = ?")) {

                try (ResultSet result = ps.executeQuery()) {
                    while (result.next()) {
                        userAuthEntity.setId(UUID.fromString(result.getString("id")));
                        userAuthEntity.setUsername(result.getString("username"));
                        userAuthEntity.setPassword(pe.encode(result.getString("password")));
                        userAuthEntity.setEnabled(result.getBoolean("enabled"));
                        userAuthEntity.setAccountNonExpired(result.getBoolean("account_non_expired"));
                        userAuthEntity.setAccountNonLocked(result.getBoolean("account_non_locked"));
                        userAuthEntity.setCredentialsNonExpired(result.getBoolean("credentials_non_expired"));
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userAuthEntity;
    }
        usersPs.setObject(1, id);
        friendsPs.setObject(1, id);
        invitesPs.setObject(1, id);
        friendsPs.executeUpdate();
        invitesPs.executeUpdate();
        usersPs.executeUpdate();

        conn.commit();
      } catch (SQLException e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
