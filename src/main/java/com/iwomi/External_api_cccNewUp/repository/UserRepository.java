
package com.iwomi.External_api_cccNewUp.repository;

import com.iwomi.External_api_cccNewUp.model.AuthorizedAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author user
 */
public interface UserRepository extends JpaRepository<AuthorizedAccounts, Integer> {
    AuthorizedAccounts findByName(String username);
    
    
    

}
