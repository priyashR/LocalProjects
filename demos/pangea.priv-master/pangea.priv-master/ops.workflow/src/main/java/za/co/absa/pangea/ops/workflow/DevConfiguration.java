package za.co.absa.pangea.ops.workflow;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix="config", name = "developerTesting",havingValue = "true", matchIfMissing = false)
public class DevConfiguration {

	@Bean
    InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {

        return new InitializingBean() {
        	@Override
            public void afterPropertiesSet() throws Exception {
            	
            	identityService.deleteMembership("john", "maker");
            	identityService.deleteMembership("jack", "checker");
            	identityService.deleteGroup("maker");
            	identityService.deleteGroup("checker");
            	identityService.deleteMembership("john", "tradeops");
            	identityService.deleteMembership("jack", "tradeops");
            	identityService.deleteGroup("tradeops");
            	identityService.deleteUser("john");
            	identityService.deleteUser("jack");
            	
                Group group = identityService.newGroup("maker");
                group.setName("Maker");
                group.setType("assignment");
                identityService.saveGroup(group);

                Group group1 = identityService.newGroup("tradeops");
                group1.setName("Trade Ops");
                group1.setType("assignment");
                identityService.saveGroup(group1);

                Group group3 = identityService.newGroup("checker");
                group3.setName("Checker");
                group3.setType("assignment");
                identityService.saveGroup(group3);

                User user = identityService.newUser("john");
                user.setPassword("john");
                user.setFirstName("John");
                user.setLastName("");
                identityService.saveUser(user);
                
                User user2 = identityService.newUser("jack");
                user2.setPassword("jack");
                user2.setFirstName("Jack");
                user2.setLastName("");
                identityService.saveUser(user2);
                
                identityService.createMembership("john", "maker");
                identityService.createMembership("jack", "checker");
                identityService.createMembership("john", "tradeops");
                identityService.createMembership("jack", "tradeops");
                
            }
        };
    }

}
