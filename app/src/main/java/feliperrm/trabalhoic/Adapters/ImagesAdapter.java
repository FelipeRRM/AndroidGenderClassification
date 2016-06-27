package feliperrm.trabalhoic.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import feliperrm.trabalhoic.Activities.CategoryActivity;
import feliperrm.trabalhoic.Activities.ManageActivity;
import feliperrm.trabalhoic.R;
import feliperrm.trabalhoic.Util.Geral;
import feliperrm.trabalhoic.Util.Singleton;


/**
 * Created by felip on 20/05/2016.
 */
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {


    Context context;
    ArrayList<String> images;
    String category;

    public ImagesAdapter(Context context, ArrayList<String> images, String category) {
        this.context = context;
        this.images = images;
        this.category = category;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(v, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        final String atual = images.get(position);

        holder.text.setText(atual);
        Glide.with(context).load(Geral.getCategoryFolderPath(category)+atual).placeholder(R.drawable.noimage)
                .crossFade().into(holder.image);

            holder.btnErase.setVisibility(View.VISIBLE);
            holder.btnErase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.delete_image)
                            .setMessage(R.string.delete_image_message)
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                   // MyEditionsAdapter.this.editions.remove(position);
                                    Singleton.getSingleton().removeFileFromCategory(atual, category);
                                    notifyItemRemoved(position);
                                    Singleton.getSingleton().setNeedsToTrainNetwork(true);
                                    Geral.deleteFile(Geral.getCategoryFolderPath(category)+atual);
                                }
                            }).show();
                }
            });
        }



    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView text;
        FrameLayout frameToElevate;
        FrameLayout btnErase;

        public ImageViewHolder(View itemView, Context context) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.editionImage);
            text = (TextView) itemView.findViewById(R.id.editionTextDate);
            frameToElevate = (FrameLayout) itemView.findViewById(R.id.frameToElevate);
            btnErase = (FrameLayout) itemView.findViewById(R.id.btnErase);
            Geral.setAnimationElevation(btnErase, btnErase, true);
        }
    }
}
