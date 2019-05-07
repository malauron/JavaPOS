package com.JavaPOS.DataAccessObjects;

public class DataAccessConstants {

    //Software Name
    public static final String SOFTWARE_NAME = "diatomspos";

    //User Groups
    public static final String TBL_USERGROUPS = "usergroups";
    public static final String COL_USERGROUP_ID = "usergroup_id";
    public static final String COL_USERGROUP_NAME = "usergroup_name";

    //Users
    public static final String TBL_USERS = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_FULLNAME = "fullname";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    //Users Photo
    public static final String TBL_USERS_PHOTOS = "users_photos";
    public static final String COL_USRFILE_SIZE = "file_size";
    public static final String COL_USRFILE_BLOB = "file_blob";

    //Suppliers
    public static final String TBL_SUPPLIERS = "suppliers";
    public static final String COL_SUPPLIER_ID = "supplier_id";
    public static final String COL_SUPPLIER_NAME = "supplier_name";
    public static final String COL_SUPPLIER_REMARKS = "remarks";

    //Suppliers Photo
    public static final String TBL_SUPPLIERS_PHOTO = "suppliers_photos";
    public static final String COL_SUPFILE_SIZE = "file_size";
    public static final String COL_SUPFILE_BLOB = "file_blob";

    //Item Groups
    public static final String TBL_ITEMGROUPS = "itemgroups";
    public static final String COL_ITEMGROUP_ID = "itemgroup_id";
    public static final String COL_ITEMGROUP_NAME = "itemgroup_name";

    //Item Sub Groups
    public static final String TBL_ITEMSUBGROUPS = "itemsubgroups";
    public static final String COL_ITEMSUBGROUP_ID = "itemsubgroup_id";
    public static final String COL_ITEMSUBGROUP_NAME = "itemsubgroup_name";

    //Unit of Measures
    public static final String TBL_UOMS = "uoms";
    public static final String COL_UOM_ID = "uom_id";
    public static final String COL_UOM_NAME = "uom_name";

    //Items
    public static final String TBL_ITEMS = "items";
    public static final String COL_ITEM_ID = "item_id";
    public static final String COL_ITEM_CODE = "item_code";
    public static final String COL_ITEM_NAME = "item_name";
    public static final String COL_ALTDESC = "alt_desc";
    public static final String COL_ITEM_CLASSIFICATION = "item_classification";
    public static final String COL_REGULARPRICE = "regularprice";
    public static final String COL_ISACTIVE = "isactive";
    public static final String COL_ITEM_DATECREATED = "date_created";
    public static final String COL_ITEM_ADDEDBYUSER = "add_by_user_id";

    //Items Photos
    public static final String TBL_ITEMS_PHOTOS = "items_photos";
    public static final String COL_ITEM_FILESIZE = "file_size";
    public static final String COL_ITEM_FILEBLOB = "file_blob";

    //Items Assemblies
    public static final String TBL_ITEMSASSEMBLIES = "items_assemblies";
    public static final String COL_EXTITEMID = "ext_item_id";
    public static final String COL_EXTITEMSUBGROUPID = "ext_itemsubgroup_id";
    public static final String COL_EXTQTY = "ext_quantity";
    public static final String COL_PRICELIMIT = "price_limit";

    // Shiftdetails
    public static final String TBL_SHIFTDETAILS = "shiftdetails";
    public static final String COL_SHIFT_ID = "shiftdetail_id";
    public static final String COL_STARTTRANS = "starttrans";
    public static final String COL_ENDTRANS = "endtrans";

    //Transactions Header
    public static final String TBL_TRANSACTIONS_HEADER = "transactions_header";
    public static final String COL_TRANSACTION_ID = "transaction_id";
    public static final String COL_CUSTOMER_ID = "customer_id";
    public static final String COL_CUSTOMERGROUP_ID = "customergroup_id";
    public static final String COL_CUSTOMERSUBGROUP_ID = "customersubgroup_id";
    public static final String COL_TOTALCASH = "total_cash";
    public static final String COL_TOTALCHECK = "total_check";
    public static final String COL_TOTALCHARGED = "total_charged";
    public static final String COL_TOTALCARD = "total_card";
    public static final String COL_TOTALPREPAID = "total_prepaid";
    public static final String COL_DISCPRCT = "total_disc_prct";
    public static final String COL_DISCAMT = "total_disc_amt";
    public static final String COL_AMOUNTDUE = "total_amount_due";
    public static final String COL_TOTALVAT = "total_vat";
    public static final String COL_CASHTENDERED = "cash_tendered";
    public static final String COL_CASHCHANGE = "cash_change";
    public static final String COL_TRNXDATE = "transaction_date";
    public static final String COL_TRNXTIME = "transaction_time";

    //Transactions Items
    public static final String TBL_TRANSACTIONS_ITEMS = "transactions_items";
    public static final String COL_LINENO = "line_no";
    public static final String COL_ITEMPRICE = "item_price";
    public static final String COL_ITEMQUANTITY = "item_quantity";
    public static final String COL_ITEMADDON = "addon";
    public static final String COL_ITEMDISCAMT = "disc_amt";
    public static final String COL_ITEMDISCPRCT = "disc_prct";
    public static final String COL_ITEMTOTAL = "item_total";

    //Transactions Items Assembly
    public static final String TBL_ITEMS_ASSEMBLIES = "transactions_items_assemblies";


}
