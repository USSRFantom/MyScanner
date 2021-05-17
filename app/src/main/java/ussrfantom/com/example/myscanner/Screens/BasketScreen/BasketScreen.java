package ussrfantom.com.example.myscanner.Screens.BasketScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ussrfantom.com.example.myscanner.R;
import ussrfantom.com.example.myscanner.Screens.CaptureScreen.CaptureScreen;
import ussrfantom.com.example.myscanner.Screens.Greeting.GreetingActivity;
import ussrfantom.com.example.myscanner.Screens.QrCode.QrCode;
import ussrfantom.com.example.myscanner.adapters.ProductAdapter;
import ussrfantom.com.example.myscanner.api.ApiFactory;
import ussrfantom.com.example.myscanner.api.ApiService;
import ussrfantom.com.example.myscanner.pojo.Product;
import ussrfantom.com.example.myscanner.pojo.ProductResponse;

public class BasketScreen extends AppCompatActivity {

    private RecyclerView recyclerViewBasket;
    private ProductAdapter adapter;
    private Disposable disposable;
    List<Product> product2;
    List<Product> productFinal;
    Button buttonScanner;
    Button buttonPay;
    public static TextView textViewPrice;
    public static String TextQr;
    String fatal = "Товар ненайден, попробуйте снова";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_screen);
        recyclerViewBasket = findViewById(R.id.recyclerViewBaskert);
        buttonScanner = findViewById(R.id.buttonScan);
        buttonPay = findViewById(R.id.buttonPay);
        textViewPrice = findViewById(R.id.textViewPrice);
        adapter = new ProductAdapter();
        adapter.setProducts(new ArrayList<>());
        recyclerViewBasket.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBasket.setAdapter(adapter);
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        product2 = new ArrayList<>();
        productFinal = new ArrayList<>();


        //удаление долгим  нажатием или просто нажитием
        adapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void OnProductClick(int position) {
               Toast.makeText(BasketScreen.this, "clicked", Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnLongClick(int position) {
                remove(position);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                remove(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewBasket);



      disposable = apiService.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProductResponse>() {
                    @Override
                    public void accept(ProductResponse productResponse) throws Exception {
                       // adapter.setProducts(productResponse.getProduct());//////<---- удалить после тестов
                        product2 = productResponse.getProduct();
                       // adapter.setProducts(product2);//////<---- удалить после тестов
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(BasketScreen.this, "Отсуствует свзяь с магазином", Toast.LENGTH_SHORT).show();
                    }
                });





        buttonScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(TextQr);//////<---- удалить после тестов
                Intent intent = new Intent(BasketScreen.this, QrCode.class);
                intent.putExtra("Key", TextQr);
                intent.putExtra("Key2", textViewPrice.getText());
                System.out.println(textViewPrice.getText());
                startActivity(intent);
            }
        });


    }

    //метод удаления элемента начало
    private void remove (int position){
        product2.remove(position);
        adapter.notifyDataSetChanged();
    }
    //метод удаления элемента конец


    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureScreen.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }



    public void Scanner (String scanner){
        for(Product product : product2) {
            if (product.getId().equals(scanner)){
                productFinal.add(product);
                adapter.setProducts(productFinal);
                String a = product.getImg();
            }else{
                ///Toast.makeText(BasketScreen.this, "Такого товара нет", Toast.LENGTH_SHORT).show(); <---
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (disposable != null){
            disposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BasketScreen.this, GreetingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);


                builder.setMessage(result.getContents());//<-------


                for(Product product : product2) {
                    if (product.getId().equals(result.getContents())){
                        builder.setTitle(product.getName());
                        builder.setMessage("Стоимость - " + product.getCost() + "руб.");

                        builder.setPositiveButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //scanCode();
                            }
                        }).setNegativeButton("Добавить в корзину", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Scanner(result.getContents());
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }else{
                        //Toast.makeText(this, fatal, Toast.LENGTH_LONG).show();
                    }
                }
            }
            else{
                Toast.makeText(this, "Товар не найден", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}



/*
        List<Product> productList = new ArrayList<>();
        Product product1 = new Product();
        Product product2 = new Product();
        product1.setName("Греча");
        product1.setId("434234234232");
        product1.setCost("404 рубля");
        product1.setDescription("Хорошая греча! берем!");
        product1.setImg("https://sovjen.ru/wp-content/uploads/2018/06/grechka.jpg");

        product2.setName("Рис");
        product2.setId("434234233332");
        product2.setCost("102 рубля");
        product2.setDescription("Хороший рис! берем!");
        product2.setImg("https://5s1.ru/wp-content/uploads/2016/07/Risovaya-dieta.jpg");

        productList.add(product1);
        productList.add(product2);
        adapter.setProducts(productList);
 */