  <?php
  include "db.php";
  include "function.php";
  $islem = isset($_GET["islem"]) ? addslashes(trim($_GET["islem"])) : null;
  $jsonArray = array(); // array değişkenimiz bunu en alta json objesine çevireceğiz.
  $jsonArray["hata"] = FALSE; // Başlangıçta hata yok olarak kabul edelim.

  $_code = 200; // HTTP Ok olarak durumu kabul edelim.
  $_method = $_SERVER["REQUEST_METHOD"]; // client tarafından bize gelen method
  // aldığımız işlem değişkenine göre işlemler yapalım.
    if($_method  == "POST") {
    }
    else if($_method == "PUT") {
    }
    else if($_method == "DELETE") {
    }
    else if($_method == "GET") {
      if(isset($_GET["tarih"]) && !empty(trim($_GET["tarih"]))) {
         $tarih = ($_GET["tarih"]);
         $tesisId= ($_GET["tesisId"]);
         $tarihKontrol = strlen($tarih);
         //if(tarih: yıl ise aylik uretim çalıştır)
        //else (tarih: yıl-ay-gun ise saatlik uretim çalıştır)
         if ($tarihKontrol == 4) {
            $url="yillikHata";
         }
         elseif ($tarihKontrol == 10) {
            $url="gunlukHata";
         }

         $bilgiler = $db->query("SELECT * FROM ".$url." where tarih like '$tarih%' && tesisId='$tesisId'", PDO::FETCH_ASSOC);
         if($bilgiler->rowCount()>0) {
           $_code = 200;
           $i=1;
           foreach( $bilgiler as $row ){
              $jsonArray["bilgi".$i] = $row;
              $i=$i+1;
           }
         }
         else {
          $_code = 400;
          $jsonArray["hata"] = TRUE; // bir hata olduğu bildirilsin.
          $jsonArray["hataMesaj"] = "Kayıt bulunamadı"; // Hatanın neden kaynaklı olduğu belirtilsin.
         }
      }
      else{
         $_code = 400;
         $jsonArray["hata"] = TRUE; // bir hata olduğu bildirilsin.
         $jsonArray["hataMesaj"] = "Lütfen tarih değişkeni gönderin"; // Hatanın neden kaynaklı olduğu belirtilsin.
      }

    }
    else {
      // hatalı bir parametre girilmesi durumunda burası çalışacak.
      $jsonArray["hata"] = TRUE; // bir hata olduğu bildirilsin.
      $jsonArray["hataMesaj"] = "Girilen İşlem Bulunmuyor."; // Hatanın neden kaynaklı olduğu belirtilsin.
    }

  SetHeader($_code);
  $jsonArray[$_code] = HttpStatus($_code);
  echo json_encode($jsonArray);
  ?>
