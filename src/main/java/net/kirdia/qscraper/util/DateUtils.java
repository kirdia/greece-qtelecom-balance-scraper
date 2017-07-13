/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.amdtelecom.support.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Kiriakos Diamantis <kd@amdtelecom.net>
 */
public class DateUtils {

    public static String getNow() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(date);
    }
}
