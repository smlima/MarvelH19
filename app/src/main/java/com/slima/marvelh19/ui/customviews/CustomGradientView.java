package com.slima.marvelh19.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.slima.marvelh19.R;

/**
 * Custom gradient view for overlaying a light dark
 * gradient on top of the character thunbnail, as per mocks
 *
 * Created by sergio.lima on 02/04/2016.
 */
public class CustomGradientView extends View {

    private float TriangleHeightInPx;
    private Paint linePaint;

    public CustomGradientView(Context context) {
        super(context);
        init(context);
    }

    public CustomGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        linePaint = new Paint();
        linePaint.setColor(context.getResources().getColor(R.color.colorCharacterDetailsLine));
        linePaint.setStrokeWidth(context.getResources().getDimension(R.dimen.dimenCharacterDetailsLine));

        TriangleHeightInPx = context.getResources().getDimension(R.dimen.dimenCharacterAngleOffset);
    }

    static final int CCONST = 0;

    @Override
    public void onDrawForeground(Canvas canvas) {

        Path path = new Path();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        path.addRect(0,0,width, height-TriangleHeightInPx-CCONST, Path.Direction.CCW);

        path.moveTo(0,height-TriangleHeightInPx-CCONST);
        path.lineTo(width, height-TriangleHeightInPx- CCONST);
        path.lineTo(width, height-CCONST);
        path.lineTo(0,height-TriangleHeightInPx-CCONST);

        canvas.clipPath(path);

        // clip the canvas before drawing it

        super.onDrawForeground(canvas);

        // this will draw a line on top of the one already existent on the CustomImageView, but... otherwise
        // will become darker and not perceptible
        canvas.drawLine(0, height - TriangleHeightInPx, width, height, linePaint);

    }
}
