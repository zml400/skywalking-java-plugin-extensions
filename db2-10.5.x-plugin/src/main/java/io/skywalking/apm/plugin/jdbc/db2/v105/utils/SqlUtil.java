package io.skywalking.apm.plugin.jdbc.db2.v105.utils;

public class SqlUtil {
    /**
     * 替换sql语句中的?
     * @param sql
     * @param params
     */
    public static String PrepareSqlTransfer(String sql,Object[] params) {
        if (params==null||params.length == 0)
            return sql;
//java.lang.ClassCastException: java.lang.Long cannot be cast to java.lang.String
        //TODO
        for (Object str : params) {
            if(str==null) {
                sql = sql.replaceFirst("\\?", "");
            }else {
                if(isDigital(str))
                    sql=sql.replaceFirst("\\?", str.toString());
                else
                    sql=sql.replaceFirst("\\?", "\""+str.toString()+"\"");
            }
        }
        return sql;
    }
    public static boolean isDigital(Object o){
        String obName = o.getClass().getName();
        String[] strs=obName.split("\\.");
        String type = strs[strs.length-1];
        if(type.equals("Short")
                ||type.equals("Integer")
                ||type.equals("Long")
                || type.equals("Float")
                ||type.equals("Double")
                ||type.equals("BigDecimal")){
            return true;
        }
        return false;
    }


//    public static void main(String[] args) {
//        String sql = "insert into product (id,description,image_url,price) values (?,?,?,?)";
//        Object[] params = new Object[4];
//        params[0]=new Integer(1);
//        params[1]="test1";
//        params[2]="fsfdsf";
//        params[3]=new BigDecimal(1000.1);
//        System.out.println(PrepareSqlTransfer(sql,params));
//    }
}
