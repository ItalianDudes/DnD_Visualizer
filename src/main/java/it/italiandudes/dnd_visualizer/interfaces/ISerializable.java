package it.italiandudes.dnd_visualizer.interfaces;

import org.json.JSONObject;

public interface ISerializable {
    String SERIALIZER_ID = "serializer_id";
    String DB_VERSION = "db_version";
    JSONObject exportElementJSON();
    String exportElement();
}
