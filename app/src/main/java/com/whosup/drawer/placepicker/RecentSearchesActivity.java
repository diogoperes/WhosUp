package com.whosup.drawer.placepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.whosup.android.whosup.R;


public class RecentSearchesActivity extends AppCompatActivity {

    private PlaceHistoryManager placeHistoryManager;
    ListView historyListView;
    PlaceInfoAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_searches);

        placeHistoryManager = PlaceHistoryManager.getInstance(this);

        historyAdapter = new PlaceInfoAdapter(this, R.drawable.ic_place_history);

        historyListView = (ListView) findViewById(R.id.history_list_view);
        historyAdapter.updateList(placeHistoryManager.getHistoryRecords());
        historyListView.setAdapter(historyAdapter);
        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceInfo placeInfo = (PlaceInfo) parent.getItemAtPosition(position);
                placeHistoryManager.updateHistory(placeInfo);

                PlaceDetail placeDetail = new PlaceDetail(placeInfo.placeId, placeInfo.getDescription(), 0.0, 0.0);
                sendResult(placeDetail);
            }
        });
    }



    private void sendResult(PlaceDetail placeDetail) {
        Intent resultIntent = new Intent();

        if(placeDetail == null) {
            finish();
            return;
        }

        resultIntent.putExtra(PlacePicker.PARAM_RESULT, placeDetail);
        resultIntent.putExtra(PlacePicker.PARAM_PLACE_ID, placeDetail.placeId);
        resultIntent.putExtra(PlacePicker.PARAM_PLACE_DESCRIPTION, placeDetail.description);
        resultIntent.putExtra(PlacePicker.PARAM_LATITUDE, placeDetail.latitude);
        resultIntent.putExtra(PlacePicker.PARAM_LONGITUDE, placeDetail.longitude);

        setResult(RESULT_OK, resultIntent);

        finish();
    }
}
