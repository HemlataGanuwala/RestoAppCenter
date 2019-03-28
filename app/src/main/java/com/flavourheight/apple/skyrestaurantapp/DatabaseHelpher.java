package com.flavourheight.apple.skyrestaurantapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.flavourheight.apple.skyrestaurantapp.Model.CartListPlanet;
import com.flavourheight.apple.skyrestaurantapp.Model.FavouritePlanet;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelpher extends SQLiteOpenHelper {

public static final String DATABSE_NAME= "Restaurant.db";

    //private final String MY_QUERY = "SELECT Amount,Amount1 FROM GIVEMONEY a INNER JOIN TAKEMONEY b ON a.Id=b.Id WHERE a.Nameg=?";
//GiveMoney Table

    public static final String TABLE_Registration="Registration";
    public static final String COL0_Registration="Id";
    public static final String COL1_Registration="FName";
    public static final String COL2_Registration="LName";
    public static final String COL3_Registration="Email";
    public static final String COL4_Registration="PhoneNo";
    public static final String COL5_Registration="Password";
    public static final String COL6_Registration="Status";

    //ItemMaster
    public static final String TABLE_Itemmaster="ItemMaster";
    public static final String COL0_Itemmaster="Itemid";
    public static final String COL1_Itemmaster="ItemName";
    public static final String COL2_Itemmaster="SubItemName";
    public static final String COL3_Itemmaster="ItemRate";
    public static final String COL4_Itemmaster="ItemType";

   //CardMaster
    public static final String TABLE_Cardmaster="CardMaster";
    public static final String COL0_Cardmaster="CID";
    public static final String COL1_Cardmaster="ItemName";
    public static final String COL2_Cardmaster="SubItemName";
    public static final String COL3_Cardmaster="ItemRate";
    public static final String COL4_Cardmaster="Username";
    public static final String COL5_Cardmaster="Password";
    public static final String COL6_Cardmaster="TotalCount";
    public static final String COL7_Cardmaster="TotalAmt";
    public static final String COL8_Cardmaster="Status";

   //Ordermaster
    public static final String TABLE_Ordermaster="OrderMaster";
    public static final String COL0_Ordermaster="OID";
    public static final String COL1_Ordermaster="CustomerName";
    public static final String COL2_Ordermaster="UserName";
    public static final String COL3_Ordermaster="ODate";
    public static final String COL4_Ordermaster="OTime";
    public static final String COL5_Ordermaster="MobileNo";
    public static final String COL6_Ordermaster="TotalAmout";
    public static final String COL7_Ordermaster="Status";
    public static final String COL8_Ordermaster="Address";
    public static final String COL9_Ordermaster="RAmount";
    public static final String COL10_Ordermaster="PaymentMode";
    public static final String COL11_Ordermaster="NoOfItem";

    public static final String TABLE_Favourite="Favourite";
    public static final String COL0_Favourite="FID";
    public static final String COL1_Favourite="UserName";
    public static final String COL2_Favourite="ItemName";
    public static final String COL3_Favourite="SubItemName";
    public static final String COL4_Favourite="ItemRate";
    public static final String COL5_Favourite="Password";
    public static final String COL6_Favourite="FavourType";


    public DatabaseHelpher(Context context) {
        super(context, DATABSE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +TABLE_Registration+ "(Id Integer  primary key autoincrement, FName Text ,LName Text, Email Text,PhoneNo Text,Password Text, Status Integer)");
        db.execSQL("create table " +TABLE_Itemmaster+ "(Itemid Integer  primary key autoincrement, ItemName Text ,SubItemName Text, ItemRate Text,ItemType Text)");
        db.execSQL("create table " +TABLE_Cardmaster+ "(CID Integer  primary key autoincrement, ItemName Text ,SubItemName Text, ItemRate Integer,ItemType Text,Username Text,Password Text, TotalCount Integer,TotalAmt Integer,Status Integer)");
        db.execSQL("create table " +TABLE_Ordermaster+ "(OID Integer  primary key autoincrement, CustomerName Text ,UserName Text, ODate Text,OTime Text,MobileNo Text,TotalAmout Text, Status Text,Address Text,RAmount Text, PaymentMode Text,NoOfItem Text)");
        db.execSQL("create table " +TABLE_Favourite+ "(FID Integer  primary key autoincrement, UserName Text, ItemName Text ,SubItemName Text, ItemRate Text,Password Text,FavourType Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE " +TABLE_Registration);
        db.execSQL(" DROP TABLE " +TABLE_Itemmaster);
        db.execSQL(" DROP TABLE " +TABLE_Cardmaster);
        db.execSQL(" DROP TABLE " +TABLE_Ordermaster);
        db.execSQL(" DROP TABLE " +TABLE_Favourite);

    }


public Boolean RegistrationData(String fname,String lname,String email,String phoneno,String password, int status)
{
    SQLiteDatabase db= getWritableDatabase();

    ContentValues contentValues=new ContentValues();

    contentValues.put(COL1_Registration,fname);
    contentValues.put(COL2_Registration,lname);
    contentValues.put(COL3_Registration,email);
    contentValues.put(COL4_Registration,phoneno);
    contentValues.put(COL5_Registration,password);
    contentValues.put(COL6_Registration,status);


    long result = db.insert(TABLE_Registration, null, contentValues);

    if (result == -1) {
        return false;
    } else {
        return true;
    }
}

    public Boolean UpdateRegistrationData(String email, int status)
    {
        SQLiteDatabase db= getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put(COL6_Registration,status);


        long result = db.update(TABLE_Registration,contentValues, "Email = ?", new String[] {email});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean FavouruteData(String username,String itemname,String subitemname,String itemrate, String password,String favimage)
    {
        SQLiteDatabase db= getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put(COL1_Favourite,username);
        contentValues.put(COL2_Favourite,itemname);
        contentValues.put(COL3_Favourite,subitemname);
        contentValues.put(COL4_Favourite,itemrate);
        contentValues.put(COL5_Favourite,password);
        contentValues.put(COL6_Favourite,favimage);


        long result = db.insert(TABLE_Favourite, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getFourData(String user,String pass)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from Favourite Where Username = ? And Password = ? ",  new String[]{user,pass});
        return cursor;
    }

    public List<FavouritePlanet> getFavouritelist(String user, String pass) {
        SQLiteDatabase db = getWritableDatabase();
        FavouritePlanet planet = null;
        List<FavouritePlanet>  mPlanetlis = new ArrayList<>();
        Cursor cursor = db.rawQuery("select ItemName,SubItemName,ItemRate,FavourType from Favourite Where Username = ? And Password = ?" , new String[]{user,pass});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            planet = new FavouritePlanet(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            mPlanetlis.add(planet);
            cursor.moveToNext();
        }

        cursor.close();
        return  mPlanetlis;

    }

    public Boolean CardData(String itemname,String subitem1,String rate1,String user,String pass,int totalcount,String totalamt,int status)
    {
        SQLiteDatabase db= getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put(COL1_Cardmaster,itemname);
        contentValues.put(COL2_Cardmaster,subitem1);
        contentValues.put(COL3_Cardmaster,rate1);
        contentValues.put(COL4_Cardmaster,user);
        contentValues.put(COL5_Cardmaster,pass);
        contentValues.put(COL6_Cardmaster,totalcount);
        contentValues.put(COL7_Cardmaster,totalamt);
        contentValues.put(COL8_Cardmaster,status);

        long result = db.insert(TABLE_Cardmaster, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean UpdateCartMasterData(String email,String subitem,String editcount,String totamt)
    {
        SQLiteDatabase db= getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put(COL2_Cardmaster,subitem);
        contentValues.put(COL6_Cardmaster,editcount);
        contentValues.put(COL7_Cardmaster,totamt);

        long result = db.update(TABLE_Cardmaster,contentValues, "Username = ?", new String[] {email});

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<CartListPlanet> getcardlist(String user) {
        SQLiteDatabase db = getWritableDatabase();
        CartListPlanet planet = null;
        List<CartListPlanet>  mPlanetlis = new ArrayList<>();
        Cursor cursor = db.rawQuery("select SubItemName,ItemRate,TotalCount,TotalAmt from CardMaster Where Username = ?", new String[]{user});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            planet = new CartListPlanet(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            mPlanetlis.add(planet);
            cursor.moveToNext();
        }

        cursor.close();
        return  mPlanetlis;

    }


//    public Boolean UpdateGiveTakeData(String id, String name, String amount, String des, String cdate, String duedate, String type)
//    {
//        SQLiteDatabase db= getWritableDatabase();
//
//        ContentValues contentValues=new ContentValues();
//        contentValues.put(COL0_GIVETAKE,id);
//        contentValues.put(COL1_GIVETAKE,name);
//        contentValues.put(COL2_GIVETAKE,amount);
//        contentValues.put(COL3_GIVETAKE,des);
//        contentValues.put(COL4_GIVETAKE,cdate);
//        contentValues.put(COL5_GIVETAKE,duedate);
//        contentValues.put(COL6_GIVETAKE,type);
//
//        db.update(TABLE_GIVETAKE, contentValues, "Id = ?", new String[]{id});
//
//       return true;
//    }




    public Cursor GetRegData()
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from Registration", null);
        return cursor;
    }

    public Cursor GetMobileData(String user)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from Registration where Email=?", new String[]{user});
        return cursor;
    }








}
