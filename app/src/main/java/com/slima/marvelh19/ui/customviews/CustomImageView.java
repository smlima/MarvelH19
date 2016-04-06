package com.slima.marvelh19.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.slima.marvelh19.R;

/**
 * Custom imageview to draw a character image but crop it a little
 * in the bottom as per wireframes
 *
 *
 * Created by sergio.lima on 02/04/2016.
 */
public class CustomImageView extends ImageView {

    private float TriangleHeightInPx;
    private Paint linePaint;


    public CustomImageView(Context context) {
        super(context);
        init(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        linePaint = new Paint();
        linePaint.setColor(context.getResources().getColor(R.color.colorCharacterDetailsLine));
        linePaint.setStrokeWidth(context.getResources().getDimension(R.dimen.dimenCharacterDetailsLine));

        TriangleHeightInPx = context.getResources().getDimension(R.dimen.dimenCharacterAngleOffset);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Path path = new Path();

        int width = canvas.getWidth();
        int height = canvas.getHeight();


        path.addRect(0,0,width, height-TriangleHeightInPx, Path.Direction.CCW);

        path.moveTo(0,height-TriangleHeightInPx);
        path.lineTo(width, height-TriangleHeightInPx);
        path.lineTo(width, height);
        path.lineTo(0,height-TriangleHeightInPx);

        canvas.clipPath(path);

        // clip the canvas before drawing it

        super.onDraw(canvas);

        //draw the line after canvas is drawn
        canvas.drawLine(0, height - TriangleHeightInPx, width, height, linePaint);


    }
}
