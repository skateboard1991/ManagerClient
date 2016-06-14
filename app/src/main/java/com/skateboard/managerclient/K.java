package com.skateboard.managerclient;

/**
 * Created by skateboard on 16-5-18.
 */
public class K
{
    //server path
    public static final String SERVER_PATH="http://10.10.20.190/flowcontrol/managerclient/";
    public static final String ORDERS_PATH=SERVER_PATH+"orders.php";
    public static final String SPLASHIMG_PATH=SERVER_PATH+"pic/splash.jpg";
    public static final String ADDORDER_PATH=SERVER_PATH+"addorder.php";
    public static final String LOGIN_PATH=SERVER_PATH+"login.php";
    public static final String ORDER_DETAIL_PATH=SERVER_PATH+"orderdetail.php";
    public static final String ORDER_STATE_PATH=SERVER_PATH+"orderstate.php";
    public static final String ADD_STEPS_PATH=SERVER_PATH+"steps.php";
    //constant
    public static final String HAS_SIGNED_IN="has_signed_in";
    public static final String ORDER_LIST="order_list";
    public static final String PROGRESS_MESSAGE="progress_message";
    public static final String ORDERNUMBER="ordernumber";

    //sql data
    public static final String DBORDERNUMBER="ordernumber";
    public static final String DBTYPE="type";
    public static final String DBVOLTAGE="voltage";
    public static final String DBSIZE="size";
    public static final String DBAMOUNT="amount";
    public static final String DBSTATE="state";
    public static final String DBNOTE="note";

    //push server
    public static final String APPID="2882303761517473531";
    public static final String APPKEY="5121747323531";
    public static final String USER_ACCOUNT="manager";
    public static final String ON_MESSAGE_ARRIVED="on_message_arrived";
    public static final String PUSH_REGISTER_FAILED="push_register_failed";
    public static final String PUSH_REGISTER_SUCCESS="push_register_success";
    public static final String SET_USER_ACCOUNT_FAILED="set_user_account_failed";
    public static final String SET_USER_ACCOUNT_SUCCESS="set_user_account_success";
}
