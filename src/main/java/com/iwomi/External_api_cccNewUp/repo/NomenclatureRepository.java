package com.iwomi.External_api_cccNewUp.repo;

import com.iwomi.External_api_cccNewUp.Entities.Nomenclature;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author User
 */
public interface NomenclatureRepository extends JpaRepository<Nomenclature, Long> {

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.acscd=?2 and e.dele=?3 ORDER BY acscd ASC", nativeQuery = true)
    Nomenclature findTabcdAndAcsd(String string, String access_code_patner, String string0);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.lib3 = ?2 order by lib7 ASC", nativeQuery = true)
    List<Nomenclature> getHomePageMenu(String string, String lib3);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.lib8 = ?2 and e.lib7 = ?3 ORDER BY lib7 ASC", nativeQuery = true)
    Nomenclature findTabcdAndLevelAndRang(String tabcd, String lib8, String lib7);
    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.lib8 = ?2 ORDER BY lib7 ASC", nativeQuery = true)
    List<Nomenclature> findTabcdAndLevel(String tabcd, String lib8);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.acscd=?2 and e.dele=?3 and e.cetab=?4 ORDER BY acscd ASC", nativeQuery = true)
    Nomenclature findTabcdAndAcsd1(String string, String access_code_patner, String string0, String etab);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.dele=?2  ORDER BY tabcd ASC", nativeQuery = true)
    List<Nomenclature> findTabcdAndDel(String string, String string0);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.dele=?2 and e.cetab=?3 ORDER BY tabcd ASC", nativeQuery = true)
    List<Nomenclature> findTabcdAndDel1(String string, String string0, String etab);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.lib5=?2 and e.dele=?3 and e.cetab=?4 ORDER BY acscd ASC", nativeQuery = true)
    List<Nomenclature> findTabcdAndLib5AndDel(String tabcd, String code_cpt, String string, String etab);

    @Transactional
    @Modifying
    @Query(value = "UPDATE sanm SET dele=?3 WHERE tabcd = ?1 and dele=?2 and cetab=?4", nativeQuery = true)
    void deleTabcd(String tabcd, String dele, String dele2, String etab);

    /********************************From another source ******************************/
    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='0012' and dele=0 and e.cetab=?1 ORDER BY acscd ASC", nativeQuery = true)
    List<Nomenclature> findInNomenclature(String etab);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.acscd=?2 and e.dele=?3 ", nativeQuery = true)
    Nomenclature findUrl1(String tabcd, String acscd, String dele);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.lib3=?2 or e.lib3=?3 and e.nfac=?4 and e.cetab=?5 ORDER BY acscd ASC", nativeQuery = true)
    List<Nomenclature> findAction(String tabcd, String lib3, String lib31, String nfac, String etab);


    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='5011' and dele=0 and e.cetab=?1", nativeQuery = true)
    Nomenclature findInNomenclatureEmail(String etab);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='5011' and e.cetab='001' and e.cetab=?1 and nfac='admin' and dele=0 ORDER BY acscd ASC", nativeQuery = true)
    List<Nomenclature> findInNomenclatureVal(String etab);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='0012' and acscd='0214' and dele=0 and e.cetab=?1", nativeQuery = true)
    Nomenclature findInNomenclatureFrais(String etab);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='5011'  and nfac='admin' and lib3=?1 and dele=0 and e.cetab=?2", nativeQuery = true)
    Nomenclature findInNomenclatureVal01(String profil, String etab);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='5011' and e.cetab='001' and nfac='admin' and lib2=?1 and dele=0 and e.cetab=?2", nativeQuery = true)
    Nomenclature findInNomenclatureLev(String lev, String etab);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='0002'  and nfac='admin' and acscd=?1 and dele=0 and e.cetab=?2", nativeQuery = true)
    Nomenclature findInNomenclatureVal02(String profil, String etab);

    //;
    @Query(value = "select IFNULL((case when lib4='0' then (((lib3 + 0.0)*?2)/100) else lib4+ 0.0 end),0.0) fee from sanm where tabcd='5006' and e.cetab=?3 and lib1=?1 and ?2 between mnt1 and mnt2 and dele=0", nativeQuery = true)
    Double findInNomenclatureMontant(String mdp, BigDecimal mnt, String etab);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd ='5011' and e.cetab=?1 and e.cetab='001' and nfac='admin' and lib2='1' and dele=0", nativeQuery = true)
    Nomenclature findInNomenclatureLib3(String etab);

    @Query(value = "SELECT * FROM sanm e where  e.cetab=?1 ORDER BY e.id ASC LIMIT 1 ", nativeQuery = true)
    Nomenclature findSanm(String etab);


    /// store procedure parameters :
    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.lib1=?2  and e.dele='0' ", nativeQuery = true)
    Nomenclature getOpeType(String string, String string0);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.acscd=?2  and e.dele='0'", nativeQuery = true)
    Nomenclature getNomSystem(String string, String string0);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.acscd=?2  and e.dele='0'", nativeQuery = true)
    Nomenclature getcodelient(String string, String string0);

    @Query(value = "SELECT * FROM sanm e WHERE e.tabcd = ?1 and e.acscd=?2  and e.dele='0' and e.cetab=?3", nativeQuery = true)
    Nomenclature getNatureD(String tabcd, String acscd, String etab);

    @Query(value = "select IFNULL((case when lib4='0' then (((lib3 +0.0)*?2)/100) else lib4+ 0.0 end),0.0) fee from sanm where tabcd='5006' and e.cetab=?4 and lib1=?1 and ?2 between mnt1 and mnt2 and dele=0", nativeQuery = true)
    Double findInNomenclatureMontantClCCC(String mdp, BigDecimal mnt, String nfact, String etab);
}
