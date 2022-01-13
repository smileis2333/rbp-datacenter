package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.channel.Channelorganization;
import com.regent.rbp.api.dto.validate.ChannelOrganizationCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2022/01/13
 * @description
 */
@Component
public class ChannelOrganizationCheckValidator implements ConstraintValidator<ChannelOrganizationCheck, Channelorganization> {
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public boolean isValid(Channelorganization channelorganization, ConstraintValidatorContext context) {
        if (channelorganization != null) {

            String o1 = channelorganization.getOrganization1();
            String o2 = channelorganization.getOrganization2();
            String o3 = channelorganization.getOrganization3();
            String o4 = channelorganization.getOrganization4();
            String o5 = channelorganization.getOrganization5();

            if (true) {
                if (StrUtil.isNotBlank(o5) && !StrUtil.isAllNotBlank(o1, o2, o3, o4)) {
                    return false;
                }
                if (StrUtil.isNotBlank(o4) && !StrUtil.isAllNotBlank(o1, o2, o3)) {
                    return false;
                }
                if (StrUtil.isNotBlank(o3) && !StrUtil.isAllNotBlank(o1, o2)) {
                    return false;
                }
                if (StrUtil.isNotBlank(o2) && !StrUtil.isAllNotBlank(o1)) {
                    return false;
                }
                if (StrUtil.isBlank(o1)) {
                    return false;
                }
                String sqlFragement = "select\n" +
                        "o1.id o1id,o1.name o1name,\n" +
                        "o2.id o2id,o2.name o2name,\n" +
                        "o3.id o3id,o3.name o3name,\n" +
                        "o4.id o4id,o4.name o4name,\n" +
                        "o5.id o5id,o5.name o5name\n" +
                        "from\n" +
                        "\trbp_channel_organization  o1 \n" +
                        "left join rbp_channel_organization  o2 on o1.id = o2.parent_id \n" +
                        "left join rbp_channel_organization  o3 on o2.id = o3.parent_id \n" +
                        "left join rbp_channel_organization  o4 on o3.id = o4.parent_id \n" +
                        "left join rbp_channel_organization  o5 on o4.id = o5.parent_id";
                String sqlCondition = String.format("where o1.name = '%s' ", o1);
                if (StrUtil.isNotBlank(o2)) {
                    sqlCondition += String.format(" and o2.name = '%s' ", o2);
                    if (StrUtil.isNotBlank(o3)) {
                        sqlCondition += String.format(" and o3.name = '%s' ", o3);
                        if (StrUtil.isNotBlank(o4)) {
                            sqlCondition += String.format(" and o4.name = '%s' ", o4);
                            if (StrUtil.isNotBlank(o5)) {
                                sqlCondition += String.format("  and o5.name = '%s' ", o5);

                            }
                        }
                    }
                }

                String sql = sqlFragement+sqlCondition;
                List<Map<Object, Object>> listMap = baseDbDao.getListMap(sql);


                return false;
            } else {
                return false;
            }
        }
        return true;
    }
}
