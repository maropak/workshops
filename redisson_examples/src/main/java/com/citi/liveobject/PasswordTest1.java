package com.citi.liveobject;

import org.redisson.Redisson;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class PasswordTest1 {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("tpc://169.172.130.135:6379").setPassword("hophop123");
        RedissonClient client = Redisson.create(config);

        RLiveObjectService service = client.getLiveObjectService();
        service.registerClass(Password.class);
        String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv2PzD/WUP0UGMoAvYNZrDGwELkj+s4UTvid+F5dMkvjV71O7fv+sC9W/DtAyXzqSvcCytk364CgYLy17YGhFUnYa2nvlpj2h2uJUyFCGc3E2YJD+U/BAlckcMKj84YqN7gzh+TBVNqlbVxZ1LBC/LuEzV4qamfrB2rhQFTpY6dHIjslSMMo8zXUTCvhLBoOF5ch9Kk+GYX11vLdg9J5/Zvk/LUbmWn2OvADqbPAfKY+buAD/ZKu6kwfzHy3dwjg2L+rBJbQpc7GM9ZPqnPEsfy6/Zg0o92jyKLajKbzmxOLpyVpfr/dmSavMmHG34RnI0DfHxCREGyyCPrtwkfBsPQIDAQAB" + ".abc.database1";
        Password password = service.get(Password.class, key);
        if (password == null) {
            password = new Password();
            password.setKey(key);
            password.setPassword("value1");
            service.persist(password);
        } else {
            password.setPassword("value2");
        }
    }
}
