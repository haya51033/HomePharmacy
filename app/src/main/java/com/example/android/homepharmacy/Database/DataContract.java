package com.example.android.homepharmacy.Database;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataContract {

    public static final String AUTHORITY ="com.example.android.homepharmacy";
    public static final Uri BASE_CONTENT_URI =Uri.parse("content://"+AUTHORITY);
    public static final String PATH_USER = "app_users";
    public static final String PATH_MEMBERS = "app_members";
    public static final String PATH_DRUGS ="drugs";
    public static final String PATH_DRUG_LIST ="drug_list";
    public static final String PATH_FIRST_AID ="first_aid";

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String TABLE_NAME = "app_users_table";
        public static final String COLUMN_USER_NAME  = "user_name";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_REMINDER_QUESTION = "reminder_question";
        public static final String COLUMN_REMINDER_QUESTION_NUM = "reminder_question_num";
        public static final String COLUMN_IS_LOGGED = "is_logged";


    }
    public static final class MemberEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEMBERS).build();

        public static final String TABLE_NAME = "app_members_table";
        public static final String COLUMN_USER_ID = "member_user_id";
        public static final String COLUMN_MEMBER_NAME  = "member_name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_PREGNANT = "pregnant";

    }


    public static final class DrugsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DRUGS).build();

        public static final String TABLE_NAME = "drugs_table2";
        public static final String COLUMN_DRUG_COMMERCIAL_NAME  = "commercial_name";
        public static final String COLUMN_DRUG_SCIENTIFIC_NAME  = "scientific_name";
        public static final String COLUMN_DRUG_INDICATION = "indication";
        public static final String COLUMN_EXPIRY_DATE = "expiry_date";
        public static final String COLUMN_DRUG_CONCENTRATION = "concentration";
        public static final String COLUMN_DRUG_TYPE = "type";
        public static final String COLUMN_DRUG_WARNINGS = "warnings";
        public static final String COLUMN_SIDE_EFFECTS = "side_effects";
        public static final String COLUMN_PREGNENT_ALLOWED = "pregnant_allowed";
        public static final String COLUMN_DRUG_DESCRIPTION = "drug_description";
        public static final String COLUMN_DRUG_BARCODE = "drug_barcode";

        //Arabic:
        public static final String COLUMN_DRUG_COMMERCIAL_NAME_ARABIC  = "commercial_name_arabic";
        public static final String COLUMN_DRUG_SCIENTIFIC_NAME_ARABIC  = "scientific_name_arabic";
        public static final String COLUMN_DRUG_INDICATION_ARABIC = "indication_arabic";
        public static final String COLUMN_DRUG_TYPE_ARABIC = "type_arabic";
        public static final String COLUMN_DRUG_WARNINGS_ARABIC = "warnings_arabic";
        public static final String COLUMN_SIDE_EFFECTS_ARABIC = "side_effects_arabic";
        public static final String COLUMN_DRUG_DESCRIPTION_ARABIC = "drug_description_arabic";

    }

    public static final class DrugListEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DRUG_LIST).build();

        public static final String TABLE_NAME = "drug_list_table";
        public static final String COLUMN_MEMBER_L_ID  = "member_list_id";
        public static final String COLUMN_DRUG_L_ID  = "drug_list_id";
        public static final String COLUMN_DRUG_START_DATE  = "start_date";
        public static final String COLUMN_DRUG_END_DATE  = "end_date";
        public static final String COLUMN_DRUG_DOSE_REPEAT  = "drug_dose_repeat";
        public static final String COLUMN_DRUG_DOSE_QUANTITY  = "drug_dose_quantity";
        public static final String COLUMN_DRUG_DOSE_DESCRIPTION = "drug_dose_description";
        public static final String COLUMN_DRUG_FIRST_TIME = "drug_first_time";

    }

    public static final class FirstAidEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FIRST_AID).build();

        public static final String TABLE_NAME = "first_aid_table";
        public static final String COLUMN_FIRST_AID_TITLE  = "first_aid_title";
        public static final String COLUMN_FIRST_AID_DESCRIPTION  = "first_aid_description";
        public static final String COLUMN_FIRST_AID_IMAGE  = "first_aid_image";
        public static final String COLUMN_FIRST_AID_LINK  = "first_aid_link";
        public static final String COLUMN_FIRST_AID_DRUG  = "first_aid_drug";
        public static final String COLUMN_FIRST_AID_TITLE_ARABIC  = "first_aid_title_arabic";
        public static final String COLUMN_FIRST_AID_DESCRIPTION_ARABIC  = "first_aid_description_arabic";

    }

}
