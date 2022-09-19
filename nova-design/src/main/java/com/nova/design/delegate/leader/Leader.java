package com.nova.design.delegate.leader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom on 2018/3/14.
 */
public class Leader implements ITarget {

    private Map<String, ITarget> targets = new HashMap<String, ITarget>();

    public Leader() {
        targets.put("加密", new TargetA());
        targets.put("登录", new TargetB());
    }

    //项目经理自己不干活
    @Override
    public void doing(String command) {
        targets.get(command).doing(command);
    }

}
