package com.sgs.mylibrary.log_interface;


public interface ConfigurationListener {
    public void beforeConfigure();

    public void afterConfigure();

    public void noRemoteConfigureFound();

    public void failedToConfigure();
}
