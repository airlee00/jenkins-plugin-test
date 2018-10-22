package org.jvnet.hudson.test;

import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.database.GlobalDatabaseConfiguration;

import hudson.Extension;
import hudson.model.RootAction;

@Extension
public class SleepAction implements RootAction {

	GlobalDatabaseConfiguration GlobalDatabaseConfiguration;
	@Override
	public String getIconFileName() {
		// TODO Auto-generated method stub
		return "gear.png";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return "Sleep Action";
	}

	@Override
	public String getUrlName() {
		// TODO Auto-generated method stub
		return "sleep-action";
	}


    public String getName() {
		return "SleepAction.java";
	}

	public static List<Columns> getAllColumns() {
        List<Columns> r = new ArrayList<Columns>();
        for (int i=0 ; i<10  ; i++ ) {
           Columns col = new Columns("col1", "col2", "col3", "col4", "col5", i);
           r.add(col);
        }
        return r;
    }
}
