package DAO.Const;

public class SqlScriptConst {

    public static final String USER_SAVE = """
            INSERT INTO users (email,password,first_name,last_name,role,date_of_birth)
            VALUES(?,?,?,?,?,?)
            """;

    public static final String USER_FIND_BY_EMAIL= """
            SELECT * FROM users WHERE email = ?
            """;

    public static final String USER_FIND_ALL= """
            SELECT * FROM users
            """;

    public static final String USER_FIND_BY_ID = """
            SELECT * FROM users WHERE id = ?
            """;

    public static final String USER_UPDATE = """
            UPDATE users
                SET email = ?, password = ?, first_name = ?, last_name = ?, role = ?, updated_date = CURRENT_TIMESTAMP
                WHERE id = ?,date_of_birth = ?;
            """ ;
    public static final String USER_DELETE = """
            DELETE FROM users WHERE id = ?
            """;

    public static final String VEHICLE_SAVE = """
            INSERT INTO vehicles(brand,model,type,status,hourly_rate,daily_rate,weekly_rate,monthly_rate,vehicle_value)
            VALUES(?,?,?,?,?,?,?,?,?)
            """;
    public static final String VEHICLE_FIND_BY_ID = """
            SELECT * FROM vehicles WHERE id = ?
            """;
    public static final String VEHICLE_FIND_ALL= """
            SELECT * FROM vehicles
            """;
    public static final String VEHICLE_UPDATE= """
            UPDATE vehicles
            SET brand = ? , model = ? , type = ? , status = ? ,hourly_rate = ?
            , daily_rate = ? , weekly_rate = ? , monthly_rate = ? , vehicle_value = ? , updated_date = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

    public static final  String VEHICLE_DELETE = """
            DELETE FROM vehicles WHERE id = ?
            """;

    public static final String VEHICLE_FIND_BY_MODEL = """
            SELECT * FROM vehicles WHERE model = ? 
            """;

    public static final String RENTAL_SAVE = """
            INSERT INTO rentals (user_id, vehicle_id, rental_start_date, rental_end_date, rent_type, deposit, total_price)
            VALUES(?,?,?,?,?,?,?)
            """;

    public static final String RENTAL_FIND_BY_ID = """
            SELECT * FROM rentals WHERE id = ?
            """;

    public static final String RENTAL_FIND_ALL = """
            SELECT * FROM rentals
            """;

    public static final String RENTAL_UPDATE = """
            UPDATE rentals
            SET user_id = ?, vehicle_id = ?, rental_start_date = ?, rental_end_date = ?,
            rent_type = ?, deposit = ?, total_price = ?, updated_date = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

    public static final String RENTAL_DELETE = """
            DELETE FROM rentals WHERE id = ?
            """;


}
