package io.renren.modules.admin;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ITMX on 2018/1/9.
 */
public interface Constants {

    List<String> WITHDRAWALS_USER_INFO_ERROR = Arrays.asList("PAYEE_NOT_EXIST","PAYEE_USER_INFO_ERROR","PAYER_USER_INFO_ERROR","PAYEE_ACC_OCUPIED","PERMIT_NON_BANK_LIMIT_PAYEE","PERM_AML_NOT_REALNAME_REV");

}
