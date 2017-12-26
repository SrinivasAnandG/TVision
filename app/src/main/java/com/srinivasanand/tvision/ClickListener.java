package com.srinivasanand.tvision;

import android.view.View;

/**
 * Created by srinivasanand on 24/09/17.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
