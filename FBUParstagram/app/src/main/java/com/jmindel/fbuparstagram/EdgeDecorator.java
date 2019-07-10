package com.jmindel.fbuparstagram;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by anthonykiniyalocts on 12/8/16.
 * Credit: https://gist.github.com/AKiniyalocts/5a00d66f03f1c3393c1302bea73749b2
 *
 * Quick way to add padding to first and last item in recyclerview via decorators
 */

public class EdgeDecorator extends RecyclerView.ItemDecoration {

    private final int edgePadding;
    private final boolean changeFirst, changeLast;

    /**
     * EdgeDecorator
     * @param edgePadding padding set on the left side of the first item and the right side of the last item
     */
    public EdgeDecorator(int edgePadding, boolean changeFirst, boolean changeLast) {
        this.edgePadding = edgePadding;
        this.changeFirst = changeFirst;
        this.changeLast = changeLast;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int itemCount = state.getItemCount();

        final int itemPosition = parent.getChildAdapterPosition(view);

        // no position, leave it alone
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }

        // first item
        if (changeFirst && itemPosition == 0) {
            outRect.set(view.getPaddingLeft(), view.getPaddingTop() + edgePadding, view.getPaddingRight(), view.getPaddingBottom());
        }
        // last item
        else if (changeLast && itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.set(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom() + edgePadding);
        }
        // every other item
        else {
            outRect.set(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }
    }
}
