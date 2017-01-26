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
                
                string strCmd = @"SELECT kor_id, kor_username, kor_lozinka, kor_ime, trenutna.lok_naziv AS trenutna_lokacija, uobicaj.lok_id, uobicaj.lok_naziv AS kor_lokacija, kat_naziv FROM korisnici LEFT JOIN lokacije AS trenutna ON kor_trenutna_lokacija = trenutna.lok_id LEFT JOIN lokacije AS uobicaj ON kor_lokacija = uobicaj.lok_id LEFT JOIN kategorije_prostorija ON uobicaj.lok_kategorija = kat_id WHERE kor_username = @username AND kor_lozinka = @lozinka";
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
                                logdUser.Id = int.Parse(data["kor_id"].ToString());
                                logdUser.userName = data["kor_username"].ToString();
                                logdUser.passWord = data["kor_lozinka"].ToString();
                                logdUser.name = data["kor_ime"].ToString();
                                logdUser.locationId = int.Parse(data["lok_id"].ToString());
                                logdUser.locationName = data["kor_lokacija"].ToString();
                                logdUser.sector = data["kat_naziv"].ToString();
                                logdUser.currentLocarion = data["trenutna_lokacija"].ToString();

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
