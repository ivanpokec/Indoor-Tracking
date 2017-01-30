using IndoorTracking.Models;
using IndoorTracking.Services;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace IndoorTracking.Controllers
{
    public class UserUpdateNotificationController : ApiController
    {
        [HttpPost]
        public IHttpActionResult UpdateNotification([FromBody] notificationUpdate userUpdate)
        {
            int usrId = int.Parse(userUpdate.usrId.ToString());
            int usrNotification = int.Parse(userUpdate.notification.ToString());
           

            User updateUser = new User();
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();

                string strCmd = @"UPDATE korisnici SET kor_notification = @notify WHERE kor_id = @userId; 
                                    SELECT kor_id, 
		                                kor_username, 
		                                kor_lozinka,
		                                kor_ime,
		                                kor_lokacija AS kor_lokacija_id, 
		                                uobicaj.lok_naziv AS kor_lokacija_naziv, 
		                                uobicajena.kat_naziv AS kor_lokacija_kategorija,
		                                kor_trenutna_lokacija AS trenutna_lokacija_id, 
		                                trenutna.lok_naziv AS trenutna_lokacija_naziv,
		                                trenutna_kat.kat_naziv AS trenutna_lokacija_kategorija,		
		                                kor_notification 
                                FROM korisnici 
                                LEFT JOIN lokacije AS trenutna ON kor_trenutna_lokacija = trenutna.lok_id 
                                LEFT JOIN lokacije AS uobicaj ON kor_lokacija = uobicaj.lok_id 
                                LEFT JOIN kategorije_prostorija AS uobicajena ON uobicaj.lok_kategorija = uobicajena.kat_id
                                LEFT JOIN kategorije_prostorija AS trenutna_kat ON trenutna.lok_kategorija = trenutna_kat.kat_id
                                    WHERE kor_id = @userId";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    cmd.Parameters.Add("@userId", System.Data.SqlDbType.Int).Value = usrId;
                    cmd.Parameters.Add("@notify", System.Data.SqlDbType.Int).Value = usrNotification;
                    using (SqlDataReader data = cmd.ExecuteReader())
                    {
                        if (data.Read())
                        {
                            int id = 0;
                            bool check = Int32.TryParse(data["kor_id"].ToString(), out id);
                            if (check == true && id != 0)
                            {
                                updateUser.userId = int.Parse(data["kor_id"].ToString());
                                updateUser.userName = data["kor_username"].ToString();
                                updateUser.passWord = data["kor_lozinka"].ToString();
                                updateUser.name = data["kor_ime"].ToString();
                                updateUser.locationId = int.Parse(data["kor_lokacija_id"].ToString());
                                updateUser.locationName = data["kor_lokacija_naziv"].ToString();
                                updateUser.locationCategory = data["kor_lokacija_kategorija"].ToString();
                                updateUser.currentLocationId = int.Parse(data["trenutna_lokacija_id"].ToString());
                                updateUser.currentLocationName = data["trenutna_lokacija_naziv"].ToString();
                                updateUser.currentLocationCategory = data["trenutna_lokacija_kategorija"].ToString();
                                updateUser.notification = int.Parse(data["kor_notification"].ToString());

                                return Ok(updateUser);
                            }

                        }

                        return NotFound();

                    }
                }

            }

        }
    }
}
