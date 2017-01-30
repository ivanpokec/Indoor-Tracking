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
    public class LoginController : ApiController
    {
        //User[] users = new User[] {
        //    new User {Id= 1, userName="test", passWord="test", name="pero"},
        //    new User {Id= 2, userName="test2", passWord="test2", name="pero2"}
        //};

        [HttpPost]
        public IHttpActionResult CheckLogin([FromBody] Login loginRequest)
        {
            string username = loginRequest.userName;
            string passWord = loginRequest.passWord;

            User logdUser = new User();
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                
                string strCmd = @"SELECT kor_id, 
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
                                    WHERE kor_username = @username AND kor_lozinka = @lozinka";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    cmd.Parameters.Add("@username", System.Data.SqlDbType.VarChar).Value= username;
                    cmd.Parameters.Add("@lozinka", System.Data.SqlDbType.VarChar).Value= passWord;
                    using (SqlDataReader data = cmd.ExecuteReader())
                    {
                        if (data.Read())
                        {
                            int id = 0;
                            bool check = Int32.TryParse(data["kor_id"].ToString(), out id);
                            if (check == true && id!=0)
                            {
                                
                                logdUser.userId = int.Parse(data["kor_id"].ToString());
                                logdUser.userName = data["kor_username"].ToString();
                                logdUser.passWord = data["kor_lozinka"].ToString();
                                logdUser.name = data["kor_ime"].ToString();
                                logdUser.locationId = int.Parse(data["kor_lokacija_id"].ToString());
                                logdUser.locationName = data["kor_lokacija_naziv"].ToString();
                                logdUser.locationCategory = data["kor_lokacija_kategorija"].ToString();
                                logdUser.currentLocationId = int.Parse(data["trenutna_lokacija_id"].ToString());
                                logdUser.currentLocationName = data["trenutna_lokacija_naziv"].ToString();
                                logdUser.currentLocationCategory = data["trenutna_lokacija_kategorija"].ToString();
                                logdUser.notification = int.Parse(data["kor_notification"].ToString());

                                return Ok(logdUser);
                            }
                            
                        }
                        
                        return NotFound();
                        
                    }
                }

            }
            
        }
    }
}
