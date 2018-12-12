package it.com.aop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateLog {

//    @Id
//    private Long id;                 //主键可以不要,mongo可提供
    private String operateType;      //操作的类型
    private Date operateDate;           //操作的时间
    private String oldValue;            //旧值
    private String newValue;            //新值
    private String className;           //操作的类
    private String methodName;          //操作的方法
}
