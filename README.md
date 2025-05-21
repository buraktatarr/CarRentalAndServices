
## Projenin Özellikleri

- Kullanıcılar (bireysel & kurumsal) e-posta ve şifre ile giriş yapabilir
- SHA-256 algoritması ile şifrelenmiş kullanıcı bilgileri
- Yalnızca giriş yapmış kullanıcılar araç kiralayabilir
- Araç tanımlama ve sadece ADMIN yetkisiyle ekleme
- Araç arama ve kategoriye göre filtreleme
- Önceki kiralama geçmişi görüntüleme
- Sayfalı araç listeleme
- Kiralama süreleri: saatlik, günlük, haftalık, aylık
- Kurumsal kullanıcılar için yalnızca aylık kiralama
- 2 milyon TL üzeri araçlar için:
  - En az 30 yaşında olma şartı
  - %10 depozito alınması


## Projedeki Veritabanı

PostgreSQL üzerinde aşağıdaki ilişkili tablolar kullanılır:
- users
- vehicles
- rentals
- categories

JOIN sorguları ile kiralama geçmişi ve kullanıcıya özel araç listeleri oluşturulur.


## Projenin Kurulum ve Çalıştırılması

1. Projeyi klonlayın:
   bir klasör açın ve cmd'yi çalıştırın. Ardından cmd'de alttaki satırı yazın;
   
   git clone https://github.com/kullanici-adi/arac-kiralama-uygulamasi.git

2. Kullandığınız IDE'de Open diyerek kolanlamış olduğunuz klasörü açın ve uygulamayı çalıştırın.
   
