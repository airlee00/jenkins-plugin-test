package org.jvnet.hudson.test;

import org.jenkinsci.plugins.database.Database;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;

/**
 * Example of how to define a configuration that involves database.
 *
 * <ul>
 * <li>Define a database property in your class (doesn't have to be {@link GlobalConfiguration}
 * <li>Use <tt>dropdownDescriptorSelector</tt> in the UI (see {@code config.groovy})
 * </ul>
 *
 * @author Kohsuke Kawaguchi
 */
@Extension
public class Sample extends GlobalConfiguration {
    private Database database;

    public Sample() {
        load();
    }

    @Override
    public String getDisplayName() {
        return "Demo Database Configuration";
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    /**
     * Applies the submitted configuration to this object
     * (That is, update the {@link #database} field.
     */
    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this,json);
        save();
        return true;
    }
}
