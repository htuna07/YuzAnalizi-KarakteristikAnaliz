package com.deneme.tuna.yuztanima;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deneme.tuna.yuztanima.Organlar.Burun;
import com.deneme.tuna.yuztanima.Organlar.Dudak;
import com.deneme.tuna.yuztanima.Organlar.Goz;
import com.deneme.tuna.yuztanima.Organlar.Kas;
import com.deneme.tuna.yuztanima.Organlar.Surat;
import com.deneme.tuna.yuztanima.Organlar.Yuz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.util.List;

public class ConfirmPhotoActivity extends AppCompatActivity {

    private boolean tiklanabilirlikDurumu = true;

    private Button onaylaButonu;
    private Button yenidenDeneButonu;
    private ImageView cekilenFotoImage;
    private Bitmap gelenFotoBitmap;
    private ProgressBar progressBar;

    private Surat gonderilecekSurat;

    private int cevirilmeSayisi = 0;

    private SharedPreferences sharedPreferences;

    private ImageView backButton ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_photo);



        //Geri butonu
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.click_effect));
                onBackPressed();
            }
        });

        onaylaButonu = findViewById(R.id.onaylaButton);
        yenidenDeneButonu = findViewById(R.id.yenidenDeneButton);
        cekilenFotoImage = findViewById(R.id.cekilenFoto);
        progressBar = findViewById(R.id.progressBar);

        gelenFotoBitmap = fotoyuAl();

        //İşlemi başlat
        tanimaIslemiBaslat(gelenFotoBitmap);







        //Eğer fotoğrafa organları çizdirme işlemi bittiyse onayla butonuna basınca sonuçları hesapla
        onaylaButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gonderilecekSurat != null){
                    sonuclariHesapla(gonderilecekSurat);
                }

            }
        });

        //Eğer fotoğrafa organları çizdirme işlemi bittiyse yeniden dene butonuna fotoğraf çekme işlemine dön
        yenidenDeneButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gonderilecekSurat != null){
                    Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


    }

    private void sonuclariHesapla(Surat hesaplanacakSurat) {
        new sonuclariHesapla().execute(hesaplanacakSurat);
    }

    private Bitmap fotoyuAl(){
        byte[] bytes = getIntent().getByteArrayExtra("foto");
        Bitmap gonderilecekFotoBitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return gonderilecekFotoBitmap;
    }

    //AsyncTask olarak tanıma işlemini başlat
    private void tanimaIslemiBaslat(Bitmap taninacakFoto){
        new tanimaIslemiTask().execute(taninacakFoto);
    }

    public Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onBackPressed() {
        if (!tiklanabilirlikDurumu)
            return;
        super.onBackPressed();


    }

    @Override
    protected void onResume() {
        super.onResume();
        tiklanabilirlik(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tiklanabilirlik(true);
    }






    public class tanimaIslemiTask extends AsyncTask<Bitmap,Integer, Surat> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tiklanabilirlik(false);



        }

        @Override
        protected Surat doInBackground(Bitmap... bitmaps) {
            //Bitmap türünden gelen fotoyu Firebase Ml Kit face detection kütüphanesine göndermek için gerekli ayarlar
            gelenFotoBitmap = bitmaps[0];
            FirebaseVisionFaceDetectorOptions options =
                    new FirebaseVisionFaceDetectorOptions.Builder()
                            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                            .build();

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(gelenFotoBitmap);
            FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(options);
            //Face Detection işlemini başlat
            Task<List<FirebaseVisionFace>> result =
                    detector.detectInImage(image)
                            .addOnCompleteListener(
                                    new OnCompleteListener<List<FirebaseVisionFace>>() {
                                        @Override
                                        public void onComplete(@NonNull Task<List<FirebaseVisionFace>> task) {
                                            //Başarılı olursa

                                            List<FirebaseVisionFace> faces  = task.getResult();

                                            //Eğer sadece bir surat tespit edildiyse
                                            if (!faces.isEmpty() && faces.size() == 1) {
                                                FirebaseVisionFace face = faces.get(0);

                                                // Organların noktaları
                                                List<FirebaseVisionPoint> faceContour =
                                                        face.getContour(FirebaseVisionFaceContour.FACE).getPoints();
                                                List<FirebaseVisionPoint> leftEyeBrowTop =
                                                        face.getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_TOP).getPoints();
                                                List<FirebaseVisionPoint> leftEyeBrowBottom =
                                                        face.getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_BOTTOM).getPoints();
                                                List<FirebaseVisionPoint> rightEyeBrowTop =
                                                        face.getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP).getPoints();
                                                List<FirebaseVisionPoint> rightEyeBrowBottom =
                                                        face.getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_BOTTOM).getPoints();
                                                List<FirebaseVisionPoint> leftEyeContour =
                                                        face.getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
                                                List<FirebaseVisionPoint> rightEyeContour =
                                                        face.getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints();
                                                List<FirebaseVisionPoint> noseBridge =
                                                        face.getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).getPoints();
                                                List<FirebaseVisionPoint> noseBottom =
                                                        face.getContour(FirebaseVisionFaceContour.NOSE_BOTTOM).getPoints();
                                                List<FirebaseVisionPoint> upperLipBottomContour =
                                                        face.getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).getPoints();
                                                List<FirebaseVisionPoint> upperLipTopContour =
                                                        face.getContour(FirebaseVisionFaceContour.UPPER_LIP_TOP).getPoints();
                                                List<FirebaseVisionPoint> lowerLipBottomContour =
                                                        face.getContour(FirebaseVisionFaceContour.LOWER_LIP_BOTTOM).getPoints();
                                                List<FirebaseVisionPoint> lowerLipTopContour =
                                                        face.getContour(FirebaseVisionFaceContour.LOWER_LIP_TOP).getPoints();

                                                //Mutluluk oranı
                                                double smileProb = 0;
                                                if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                    smileProb = (double) (face.getSmilingProbability() * 100);
                                                }

                                                //Gerekli nesne üretimleri
                                                Yuz yuz = new Yuz();
                                                Kas sagKas = new Kas();
                                                Kas solKas = new Kas();
                                                Burun burun = new Burun();
                                                Goz sagGoz = new Goz();
                                                Goz solGoz = new Goz();
                                                Dudak altDudak = new Dudak();
                                                Dudak ustDudak = new Dudak();

                                                //Noktaların gerekli sınıflarda tutulması
                                                for(FirebaseVisionPoint contour : faceContour){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    yuz.addYuzCevresiNoktasi(nokta);
                                                }
                                                for(FirebaseVisionPoint contour : leftEyeBrowBottom){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    solKas.addAltNokta(nokta);
                                                }
                                                for(FirebaseVisionPoint contour : leftEyeBrowTop){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    solKas.addUstNokta(nokta);
                                                }
                                                solKas.cevreOlustur();
                                                for(FirebaseVisionPoint contour : rightEyeBrowBottom){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    sagKas.addAltNokta(nokta);
                                                }
                                                for(FirebaseVisionPoint contour : rightEyeBrowTop){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    sagKas.addUstNokta(nokta);
                                                }
                                                sagKas.cevreOlustur();
                                                for(FirebaseVisionPoint contour : leftEyeContour){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    solGoz.addGozCevresiNoktasi(nokta);
                                                }
                                                for(FirebaseVisionPoint contour : rightEyeContour){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    sagGoz.addGozCevresiNoktasi(nokta);
                                                }

                                                for(FirebaseVisionPoint contour : noseBridge){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    burun.addKemerNoktasi(nokta);
                                                }
                                                for(FirebaseVisionPoint contour : noseBottom){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    burun.addAltNokta(nokta);
                                                }

                                                for(FirebaseVisionPoint contour : upperLipBottomContour){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    ustDudak.addAltNokta(nokta);
                                                }
                                                for(FirebaseVisionPoint contour : upperLipTopContour){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    ustDudak.addUstNokta(nokta);
                                                }
                                                ustDudak.cevreOlustur();
                                                for(FirebaseVisionPoint contour : lowerLipBottomContour){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    altDudak.addAltNokta(nokta);
                                                }
                                                for(FirebaseVisionPoint contour : lowerLipTopContour){
                                                    Nokta nokta = new Nokta();
                                                    nokta.setX(contour.getX());
                                                    nokta.setY(contour.getY());
                                                    altDudak.addUstNokta(nokta);
                                                }
                                                altDudak.cevreOlustur();




                                                gonderilecekSurat = new Surat();

                                                gonderilecekSurat.setYuz(yuz);
                                                gonderilecekSurat.setSagKas(sagKas);
                                                gonderilecekSurat.setSolKas(solKas);
                                                gonderilecekSurat.setSagGoz(sagGoz);
                                                gonderilecekSurat.setSolGoz(solGoz);
                                                gonderilecekSurat.setBurun(burun);
                                                gonderilecekSurat.setUstDudak(ustDudak);
                                                gonderilecekSurat.setAltDudak(altDudak);
                                                gonderilecekSurat.setMutlulukOrani(smileProb);



                                                //Kullanıcının çektiği fotoyu ekranda göster, ardından bu fotoğrafın üzerine tespit edilen noktaları çizdir
                                                cekilenFotoImage.setImageBitmap(gelenFotoBitmap);
                                                new cizdirmeIslemiTask().execute(gonderilecekSurat);













                                            }
                                            else {
                                                //Çekilen fotoğraf yan çevirilmiş olarak gelebiliyor, fotoğrafı düz çevirmek için yapılacak işlem
                                                cevirilmeSayisi++;
                                                if (cevirilmeSayisi >= 4){
                                                    Toast.makeText(getApplicationContext(),"Herhangi bir yüz tespit edilemedi veya tespit edilen yüz sayısı birden fazla. Lütfen yeniden deneyiniz.",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    onCancelled();
                                                }else {
                                                    Bitmap cevirilenFoto = RotateBitmap(gelenFotoBitmap, 90);
                                                    tanimaIslemiBaslat(cevirilenFoto);
                                                    onCancelled();
                                                }




                                            }

                                        }
                                    }
                            )

                            //İşlem başarısız olursa
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(getApplicationContext(),"Bir hata meydana geldi. Lütfen yeniden deneyiniz.",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                                            startActivity(intent);
                                            finish();
                                            onCancelled();

                                        }
                                    });


            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Surat gelenSurat) {
            super.onPostExecute(gelenSurat);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }

    }



    public class cizdirmeIslemiTask extends AsyncTask<Surat,Integer,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Surat... surats) {
            //Gelen fotoğrafın üzerine organları çizdirme işlemi
            Bitmap bitmap = gelenFotoBitmap;
            Paint paint = new Paint();
            paint.setAntiAlias(true);



            Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);


            Canvas canvas = new Canvas(mutableBitmap);

            //yuz çiz

            paint.setColor(Color.GREEN);
            for (int i = 0 ; i < surats[0].getYuz().getCevre().size()-1;i++){
                canvas.drawLine(surats[0].getYuz().getCevre().get(i).getX(),surats[0].getYuz().getCevre().get(i).getY()
                        ,surats[0].getYuz().getCevre().get(i+1).getX(),surats[0].getYuz().getCevre().get(i+1).getY(),paint);


            }

            //altdudak çiz



            for (int i = 0 ; i < surats[0].getAltDudak().getUst().size()-1;i++){
                canvas.drawLine(surats[0].getAltDudak().getUst().get(i).getX(),surats[0].getAltDudak().getUst().get(i).getY()
                        ,surats[0].getAltDudak().getUst().get(i+1).getX(),surats[0].getAltDudak().getUst().get(i+1).getY(),paint);


            }

            for (int i = 0 ; i < surats[0].getAltDudak().getAlt().size()-1;i++){
                canvas.drawLine(surats[0].getAltDudak().getAlt().get(i).getX(),surats[0].getAltDudak().getAlt().get(i).getY()
                        ,surats[0].getAltDudak().getAlt().get(i+1).getX(),surats[0].getAltDudak().getAlt().get(i+1).getY(),paint);


            }
            //ustdudak çiz


            for (int i = 0 ; i < surats[0].getUstDudak().getUst().size()-1;i++){
                canvas.drawLine(surats[0].getUstDudak().getUst().get(i).getX(),surats[0].getUstDudak().getUst().get(i).getY()
                        ,surats[0].getUstDudak().getUst().get(i+1).getX(),surats[0].getUstDudak().getUst().get(i+1).getY(),paint);


            }
            for (int i = 0 ; i < surats[0].getUstDudak().getAlt().size()-1;i++){
                canvas.drawLine(surats[0].getUstDudak().getAlt().get(i).getX(),surats[0].getUstDudak().getAlt().get(i).getY()
                        ,surats[0].getUstDudak().getAlt().get(i+1).getX(),surats[0].getUstDudak().getAlt().get(i+1).getY(),paint);


            }

            /*sag kaş çiz


            for (int i = 0 ; i < surats[0].getSagKas().getUst().size()-1;i++){
                canvas.drawLine(surats[0].getSagKas().getUst().get(i).getX(),surats[0].getSagKas().getUst().get(i).getY()
                        ,surats[0].getSagKas().getUst().get(i+1).getX(),surats[0].getSagKas().getUst().get(i+1).getY(),paint);


            }

            for (int i = 0 ; i < surats[0].getSagKas().getAlt().size()-1;i++){
                canvas.drawLine(surats[0].getSagKas().getAlt().get(i).getX(),surats[0].getSagKas().getAlt().get(i).getY()
                        ,surats[0].getSagKas().getAlt().get(i+1).getX(),surats[0].getSagKas().getAlt().get(i+1).getY(),paint);


            }
            */

            /*solkaş çiz

            for (int i = 0 ; i < surats[0].getSolKas().getUst().size()-1;i++){
                canvas.drawLine(surats[0].getSolKas().getUst().get(i).getX(),surats[0].getSolKas().getUst().get(i).getY()
                        ,surats[0].getSolKas().getUst().get(i+1).getX(),surats[0].getSolKas().getUst().get(i+1).getY(),paint);


            }

            for (int i = 0 ; i < surats[0].getSolKas().getAlt().size()-1;i++){
                canvas.drawLine(surats[0].getSolKas().getAlt().get(i).getX(),surats[0].getSolKas().getAlt().get(i).getY()
                        ,surats[0].getSolKas().getAlt().get(i+1).getX(),surats[0].getSolKas().getAlt().get(i+1).getY(),paint);


            }

            */
            //sag göz çiz


            for (int i = 0 ; i < surats[0].getSagGoz().getCevre().size()-1;i++){
                canvas.drawLine(surats[0].getSagGoz().getCevre().get(i).getX(),surats[0].getSagGoz().getCevre().get(i).getY()
                        ,surats[0].getSagGoz().getCevre().get(i+1).getX(),surats[0].getSagGoz().getCevre().get(i+1).getY(),paint);


            }

            //sol göz çiz


            for (int i = 0 ; i < surats[0].getSolGoz().getCevre().size()-1;i++){
                canvas.drawLine(surats[0].getSolGoz().getCevre().get(i).getX(),surats[0].getSolGoz().getCevre().get(i).getY()
                        ,surats[0].getSolGoz().getCevre().get(i+1).getX(),surats[0].getSolGoz().getCevre().get(i+1).getY(),paint);


            }

            //burun çiz


            canvas.drawLine(surats[0].getBurun().getKemer().get(0).getX(),surats[0].getBurun().getKemer().get(0).getY(),
                    surats[0].getBurun().getKemer().get(1).getX(),surats[0].getBurun().getKemer().get(1).getY(),paint);



            canvas.drawLine(surats[0].getBurun().getAlt().get(0).getX(),surats[0].getBurun().getAlt().get(0).getY(),
                    surats[0].getBurun().getKemer().get(1).getX(),surats[0].getBurun().getKemer().get(1).getY(),paint);
            canvas.drawLine(surats[0].getBurun().getAlt().get(2).getX(),surats[0].getBurun().getAlt().get(2).getY(),
                    surats[0].getBurun().getKemer().get(1).getX(),surats[0].getBurun().getKemer().get(1).getY(),paint);


            return mutableBitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            //Çekilen fotonun üzerine çizilen noktaları bastır
            cekilenFotoImage.setAdjustViewBounds(true);
            cekilenFotoImage.setImageBitmap(bitmap);

            tiklanabilirlik(true);


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }

    public class sonuclariHesapla extends AsyncTask<Surat,Integer,AnalizSonuclari>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tiklanabilirlik(false);
        }

        @Override
        protected AnalizSonuclari doInBackground(Surat... surats) {
            //Cinsiyet seçimini al
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            //Analizi başlat
            Analiz analiz = new Analiz(surats[0],sharedPreferences.getString("cinsiyet",""));
            AnalizSonuclari analizSonuclari = analiz.analiziBaslat();
            return analizSonuclari;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(AnalizSonuclari analizSonuclari) {
            tiklanabilirlik(true);
            super.onPostExecute(analizSonuclari);
            //Analiz sonuçlarını gösteren activity'i başlat
            Intent intent = new Intent(getApplicationContext(),ShowResultsActivity.class);
            intent.putExtra("analizsonuclari",analizSonuclari);
            startActivity(intent);
            finish();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            tiklanabilirlik(true);
        }

    }

    //Kullanıcının işlem bitmeden/bitince butonlara tıklama/tıklayamama durumunu ayarlayan metod
    private void tiklanabilirlik(boolean tiklanabilirlik){
        tiklanabilirlikDurumu = tiklanabilirlik;
        if (tiklanabilirlik) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

}
