package ru.skyeng.listening;


import android.support.v4.util.Pair;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import ru.skyeng.listening.Modules.AudioFiles.model.DurationParam;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 20/02/2017.
 * Project: Listening
 * ---------------------------------------------------
 * <a href="http://www.skyeng.ru">www.skyeng.ru</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class DurationParamTest {

    @Test
    public void toStringTest(){
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        list.add(new Pair<>(0,300));
        list.add(new Pair<>(400, 500));

        DurationParam param = new DurationParam(list);
        String str = param.toString();
        assertThat("", str.equals("durations[0][0]=0&durations[0][1]=300&durations[1][0]=400&durations[1][1]=500"));
    }

}
