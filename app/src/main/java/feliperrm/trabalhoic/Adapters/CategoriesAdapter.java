package feliperrm.trabalhoic.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

import feliperrm.trabalhoic.Activities.CategoryActivity;
import feliperrm.trabalhoic.Model.Category;
import feliperrm.trabalhoic.R;
import feliperrm.trabalhoic.Util.Geral;
import feliperrm.trabalhoic.Util.Singleton;


/**
 * Created by felip on 20/05/2016.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {


    Context context;
    ArrayList<Category> categories;

    public CategoriesAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        CategoryViewHolder viewHolder = new CategoryViewHolder(v, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        final Category atual = categories.get(position);

        holder.date.setText(Geral.getDataBarrada(atual.getName()));
        Geral.setAnimationElevation(holder.frameToElevate, holder.frameToElevate, true);
        holder.frameToElevate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prox = new Intent(context, CategoryActivity.class);
                prox.putExtra(CategoryActivity.CATEGORY_POS_KEY, position);
                context.startActivity(prox);
            }
        });
        if(atual.getFiles()!= null && atual.getFiles().size()>0){
            Glide.with(context).load(atual.getFiles().get(new Random().nextInt(atual.getFiles().size()))).placeholder(R.drawable.noimage).crossFade().into(holder.image);
        }
        else{
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.noimage));
        }

            holder.btnErase.setVisibility(View.VISIBLE);
            holder.btnErase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.delete_category)
                            .setMessage(R.string.delete_category_message)
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
                                    Singleton.getSingleton().removeCategoria(position);
                                    notifyItemRemoved(position);

                                }
                            }).show();
                }
            });
        }



    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView date;
        FrameLayout frameToElevate;
        FrameLayout btnErase;

        public CategoryViewHolder(View itemView, Context context) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.editionImage);
            date = (TextView) itemView.findViewById(R.id.editionTextDate);
            frameToElevate = (FrameLayout) itemView.findViewById(R.id.frameToElevate);
            btnErase = (FrameLayout) itemView.findViewById(R.id.btnErase);
            Geral.setAnimationElevation(btnErase, btnErase, true);
        }
    }
}
