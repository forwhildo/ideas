package IdCard;

import java.time.Year;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author: wxm
 * @description: 检验身份证号入口
 * @readme:
 * @date: 2023/5/18 10:49
 */
public class IdCardUtil {


    public static boolean checkIdCode(String idCodeRaw){
        String idCode = idCodeRaw.trim();
        if(idCode == null || (idCode.length() != 15 && idCode.length() != 18)){
            return false;
        }
        if(idCode.length() == 18){
            return valid18IdCardNumber(idCode);
        }else {
            return valid15IdCardNumber(idCode);
        }

    }

    private static boolean valid18IdCardNumber(String idCode){

        if (!isDigit(idCode.substring(0,17))) {
            return false;
        }
        if (!checkAddressCode(idCode.substring(0,6))) {
            return false;
        }
        if (!checkBirthCode(idCode.substring(6,14))) {
            return false;
        }
        if (!checkCheckCode(idCode)) {
            return false;
        }
        return true;
    }

    private static boolean valid15IdCardNumber(String idCode){

        //20世纪
        String code = idCode.substring(0,6) + "19" + idCode.substring(6,15);
        if (!isDigit(code)) {
            return false;
        }
        if (!checkAddressCode(code.substring(0,6))) {
            return false;
        }
        if (checkBirthCode(code.substring(6, 14))) {
            return true;
        }
        return false;
    }

    //数字序列
    private static boolean isDigit(String str){
        String exp = "[0-9]*";
        Pattern pattern = Pattern.compile(exp);
        return pattern.matcher(str).matches();
    }

    /**
     * @description 检验地址码（省，市，县）6位 只检验省级
     * @param addressCode
     * @return boolean
     * @author wxm
     * @date 2023/5/18
     */
    private static boolean checkAddressCode(String addressCode){
        List<Integer> codes = Arrays.asList(11,12,13,14,15,21,22,23,31,32,33,34,35,36,
                37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81);
        if (!codes.contains(Integer.parseInt(addressCode.substring(0,2)))){
            return false;
        }
        return true;
    }

    /**
     * @description 检验生日 8位
     * @param birthCode
     * @return boolean
     * @author wxm
     * @date 2023/5/18
     */
    private static boolean checkBirthCode(String birthCode){
        int year = Integer.parseInt(birthCode.substring(0,4));
        int month = Integer.parseInt(birthCode.substring(4,6));
        int day = Integer.parseInt(birthCode.substring(6,8));
        return isBirthday(year,month,day);
    }

    private static boolean isBirthday(int year, int month, int day) {
        // 验证年
        int thisYear = new Date().getYear() + 1900;
        if (year < 1900 || year > thisYear) {
            return false;
        }
        // 验证月
        if (month < 1 || month > 12) {
            return false;
        }
        // 验证日
        if (day < 1 || day > 31) {
            return false;
        }
        // 检查几个大月的最大天数
        if (day == 31 && (month == 4 || month == 6 || month == 9 || month == 11)) {
            return false;
        }
        if (month == 2) {
            // 在2月，非闰年最大28，闰年最大29
            return day < 29 || (day == 29 && Year.isLeap(year));
        }
        return true;
    }


    /**
     * @description 检验最后一位校验码 1位
     * @param code
     * @return boolean
     * @author wxm
     * @date 2023/5/18
     */
    private static boolean checkCheckCode(String code){

        String exceptCheckCode = code.substring(17, 18).toLowerCase();
        String rawCode = code.substring(0,17);

        final char[] array = rawCode.toCharArray();
        //加权值
        final int[] POWER = new int[]{ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

        int sum17 = 0 ;
        for(int i = 0;i < rawCode.length();i++){
            sum17 += (array[i] - '0') * POWER[i];
        }

        String resultCheckCode = getCheckCode(sum17);

        return resultCheckCode.equals(exceptCheckCode);
    }

    /**
     * @description 根据加权和获取校验码
     * @param sum17
     * @return java.lang.String
     * @author wxm
     * @date 2023/5/18
     */
    private static String getCheckCode(int sum17){
        List<String> checkCodes = Arrays.asList("1","0","x","9","8","7","6","5","4","3","2");
        return checkCodes.get(sum17 % 11);
    }

}