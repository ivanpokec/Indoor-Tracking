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
    public class UserPassUpdateController : ApiController
    {
        [HttpPost]
        public IHttpActionResult CheckLogin([FromBody] UserUpdate userUpdate)
        {
            int usrId = int.Parse( userUpdate.usrId.ToString());
            string passWord = userUpdate.passWord;

            User logdUser = new User();
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();

                string strCmd = @"UPDATE korisnici SET kor_lozinka = @lozinka WHERE kor_id = @userId; SELECT kor_id, kor_username, kor_lozinka, kor_ime, kor_lokacija, lok_naziv FROM korisnici LEFT JOIN lokacije ON lok_id = kor_lokacija WHERE kor_id = @userId";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {
                    cmd.Parameters.Add("@userId", System.Data.SqlDbType.Int).Value = usrId;
                    cmd.Parameters.Add("@lozinka", System.Data.SqlDbType.VarChar).Value = passWord;
                    using (SqlDataReader data = cmd.ExecuteReader())
                    {
                        if (data.Read())
                        {
                            int id = 0;
                            bool check = Int32.TryParse(data["kor_id"].ToString(), out id);
                            if (check == true && id != 0)
                            {
                                logdUser.Id = id;
                                logdUser.userName = data["kor_username"].ToString();
                                logdUser.passWord = data["kor_lozinka"].ToString();
                                logdUser.name = data["kor_ime"].ToString();
                                logdUser.locationId = Int32.Parse(data["kor_lokacija"].ToString());
                                logdUser.locationName = data["lok_naziv"].ToString();

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
