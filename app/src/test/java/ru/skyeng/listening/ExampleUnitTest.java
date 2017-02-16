package ru.skyeng.listening;

import com.google.gson.Gson;

import org.junit.Test;

import ru.skyeng.listening.Modules.AudioFiles.model.AudioData;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    String json = "{\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"id\": 1,\n" +
            "            \"title\": \"How I Found the Lord\",\n" +
            "            \"description\": \"A woman talks about how she found religion again later in life and describes her current religious activities.\",\n" +
            "            \"audioFileUrl\": \"http://path/to/audio\",\n" +
            "            \"imageFileUrl\": \"http://path/to/image\",\n" +
            "            \"wordsInMinute\": 71,\n" +
            "            \"accent\": {\n" +
            "                \"title\": \"British\"\n" +
            "            },\n" +
            "            \"level\": {\n" +
            "                \"title\": \"7\",\n" +
            "                \"subtitle\": \"Разговорный\"\n" +
            "            },\n" +
            "            tags\": [\n" +
            "                {\n" +
            "                    \"title\": \"Компьютеры\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"durationInSeconds\": 600\n" +
            "        }\n" +
            "    ],\n" +
            "    \"meta\": {\n" +
            "        \"currentPage\": 1,\n" +
            "        \"lastPage\": 10\n" +
            "    }\n" +
            "}";

    @Test
    public void deserializeAudioFiles() throws Exception {
        Gson gson = new Gson();
        AudioData a = gson.fromJson(json, AudioData.class);
        System.out.println();
    }
}