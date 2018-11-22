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
       $jsonArray["giris"]=0;
     	// verilerimizi post yöntemi ile alalım.
        $kullaniciAdi1 = addslashes($_POST["kullaniciAdi1"]);
        $sifre1 = addslashes($_POST["sifre1"]);

        $uye = $db->query("SELECT * from kullanici WHERE kAdi='$kullaniciAdi1' && kSifre='$sifre1'")->fetch(PDO::FETCH_ASSOC);

        
        if($uye) {
        	  $_code = 200;
            $jsonArray["hata"] = FALSE ;  // bir hata yok.
            $jsonArray["giris"]=1;
            $jsonArray["tesisId"]= $uye['tesisId'];
          
        }else {
          $_code = 400;
           $jsonArray["hata"] =TRUE ;  
           $jsonArray["giris"]=0;
            $jsonArray["hataMesaj"] = "Kullanıcı Adı veya Şifre yanlış.";
         }
  }else if($_method == "PUT") {
  }else if($_method == "DELETE") {
  }else if($_method == "GET") {
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
