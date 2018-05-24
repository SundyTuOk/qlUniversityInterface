package com.sf.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)//表示整合JUnit4进行测试
@ContextConfiguration(locations={"classpath:config/spring/applicationContext.xml"})//加载spring配置文件
public class TuTest {

//    @Resource(name = "sessionFactory")
//    private SessionFactory sessionFactory;
//    @Resource(name = "mdataSource")
//    private ComboPooledDataSource dataSource;

    @Test
    public void test1(){
//        try {
//            Connection connection = dataSource.getConnection();
//            PreparedStatement ps = connection.prepareStatement("SELECT * FROM apsaleinfo where rownum=1");
//            ResultSet resultSet = ps.executeQuery();
//            if(resultSet.next()){
//                String saleinfonum = resultSet.getString("SALEINFONUM");
//                System.out.println(saleinfonum);
//            }
//        } catch (SQLException e) {
//            System.out.println("出错"+e.toString());
//        }
    }
}
