package com.joakimsvedin.whatsfordinner.Groceries;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import com.joakimsvedin.whatsfordinner.R;
import com.joakimsvedin.whatsfordinner.DataHandler;

abstract public class SwipeToChangeCallback extends ItemTouchHelper.Callback {

    Context mContext;
    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable plusDrawable;
    private Drawable minusDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;
    private boolean inAction = false;


    SwipeToChangeCallback(Context context) {
        mContext = context;
        mBackground = new ColorDrawable();
        backgroundColor = Color.parseColor("#c2d5da");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        plusDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_plus_button);
        minusDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_minus_button);
        intrinsicWidth = plusDrawable.getIntrinsicWidth();
        intrinsicHeight = plusDrawable.getIntrinsicHeight();

    }

    public boolean isInAction() {
        return inAction;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        int itemHeight = itemView.getHeight();
        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;

        if( dX < (-intrinsicWidth-2*deleteIconMargin)*2) {
            dX = (-intrinsicWidth-2*deleteIconMargin)*2;
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        inAction = !isCancelled;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }



        mBackground.setColor(backgroundColor);
        int max = (int)dX;
        if(dX < -intrinsicWidth-2*deleteIconMargin){
            max = -intrinsicWidth-2*deleteIconMargin;
        }

        mBackground.setBounds(itemView.getRight() + max, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        mBackground.draw(c);

        if(dX < max-2){
            mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight()+max-2, itemView.getBottom());
            mBackground.draw(c);
        }




        plusDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        plusDrawable.draw(c);

        minusDrawable.setBounds(itemView.getRight() - deleteIconMargin*3 - intrinsicWidth*2 -2, deleteIconTop,
                itemView.getRight() - deleteIconMargin*3 - intrinsicWidth -2, deleteIconBottom);
        minusDrawable.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 1f;
    }
}


