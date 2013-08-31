package com.hp.sdf.ngp.custom.sbm.storeclient.service;

import java.text.MessageFormat;
import java.util.Hashtable;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hp.sdf.ngp.api.exception.StoreClientServiceException;
import com.hp.sdf.ngp.api.thirdparty.SubscriberInfoService;
import com.hp.sdf.ngp.common.exception.NgpRuntimeException;
import com.hp.sdf.ngp.dao.SystemConfigDAO;
import com.hp.sdf.ngp.model.SystemConfig;

@Component
public class SbmSubscriberInfoServiceImpl implements SubscriberInfoService {
    
    private static String LDAP_URL_PATTERN = "ldap://{0}:{1}";
    private String[] searchAttributes = new String[] {"jphoneuseruid"};
    
    private static String serverIp ;
    private static String serverPort ;
    
	private final static Log log = LogFactory
	.getLog(SbmSubscriberInfoServiceImpl.class);
    
	@Resource
	private SystemConfigDAO systemConfigDAO;
    
    public String getSubscriberId(String msisdn) throws StoreClientServiceException {
        
        /**
        JndiObject systemConfigServiceJndiObject = this.jndiManager.lookup(JNDI_NAME_SYSTEM_CONFIG_SERVICE);
        Object serverIp = this.jndiManager.execute(systemConfigServiceJndiObject,
                API_NAME_SYSTEM_CONFIG_SERVICE_GETCONFIGVALUE, new Class[] { String.class },
                new Object[] { "CURSERVERIP" }, "ConfigKey=".concat(SYSCONFIG_KEY_CURSERVERIP));

        Object serverPort = this.jndiManager.execute(systemConfigServiceJndiObject,
                API_NAME_SYSTEM_CONFIG_SERVICE_GETCONFIGVALUE, new Class[] { String.class },
                new Object[] { "CURSERVERPORT" }, "ConfigKey=".concat(SYSCONFIG_KEY_CURSERVERPORT));
        **/
		serverIp = this.getConfigValue("CURSERVERIP");
		serverPort = this.getConfigValue("CURSERVERPORT");
        String baseDN = "dc=jp-t,dc=ne,dc=jp";
        
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, MessageFormat.format(LDAP_URL_PATTERN, new Object[] { serverIp, serverPort }));
        env.put(Context.SECURITY_AUTHENTICATION, "SIMPLE");
        env.put(Context.SECURITY_PRINCIPAL, "cn=root");
        env.put(Context.SECURITY_CREDENTIALS, "Decjapan");

        DirContext ctx;
        try {
            ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            throw new StoreClientServiceException(e);
        }
        try {
            String searchParameter = "(maillogin=".concat(msisdn).concat(")");
            SearchControls controls = new SearchControls(SearchControls.SUBTREE_SCOPE, 0, 0, searchAttributes, true,
                    true);
            NamingEnumeration<SearchResult> search = ctx.search(baseDN, searchParameter, controls);
            if (search.hasMoreElements()) {
                SearchResult nextElement = search.nextElement();
                Attributes attributes = nextElement.getAttributes();
                NamingEnumeration<? extends Attribute> all = attributes.getAll();
                if (all.hasMore()) {
                    Attribute attribute = all.next();
                    return attribute.get().toString();
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            throw new StoreClientServiceException(e);
        }

    }

	private String getConfigValue(String key) {
		try {
			SystemConfig systemConfig = systemConfigDAO.findUniqueBy("configKey", key);
			if(null != systemConfig){
				return systemConfig.getValue();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SystemConfiguration exception: " + e);
			throw new NgpRuntimeException(e);
		}
	}
	
}
