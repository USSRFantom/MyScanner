package ussrfantom.com.example.myscanner.adapters;

import android.app.DownloadManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ussrfantom.com.example.myscanner.R;
import ussrfantom.com.example.myscanner.Screens.BasketScreen.BasketScreen;
import ussrfantom.com.example.myscanner.pojo.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>  {

    private List<Product> products;
    private int price;
    private String Qr = "";
    private OnProductClickListener onProductClickListener;


    public interface OnProductClickListener {
        void OnProductClick (int position);
    }

    public void setOnProductClickListener(OnProductClickListener onProductClickListener) {
        this.onProductClickListener = onProductClickListener;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.textViewId.setText(product.getId());
        holder.textViewName.setText(product.getName());
        holder.textViewDescription.setText(product.getDescription());
        holder.textViewCost.setText(product.getCost());
        Picasso.get().load(product.getImg()).into(holder.imageViewImg);


        //обнуление данных о цене и qr коду и новое их создание начало
        price = 0;
        Qr = "";
        Log.i("Обнуление", String.valueOf(price));
        for (int a = products.size() -1; a >= 0; a--){
            Product productF = products.get(a);
            price  = price +  Integer.parseInt(productF.getCost());
            Qr = Qr + product.getId() + " ";
        }
        Log.i("Цена", String.valueOf(price));
        //обнуление данных о цене и qr коду и новое их создание конец


        //отправляем статической переменной весь список для формаирования qr кода начало
        String a = String.valueOf(price);
        BasketScreen.textViewPrice.setText(a);
                Log.i("Отправили с холдера", Qr);//////<---- удалить после тестов
        BasketScreen.TextQr = Qr;
        //отправляем статической переменной весь список для формаирования qr кода начало


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewId;
        private TextView textViewName;
        private TextView textViewDescription;
        private TextView textViewCost;
        private ImageView imageViewImg;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCost = itemView.findViewById(R.id.textViewCost);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            imageViewImg = itemView.findViewById(R.id.imageViewBasket);
            textViewId = itemView.findViewById(R.id.textViewID);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onProductClickListener != null){
                        onProductClickListener.OnProductClick(getAdapterPosition());
                    }
                }
            });
        }
    }


}
