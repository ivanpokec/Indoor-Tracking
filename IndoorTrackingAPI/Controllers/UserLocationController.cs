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
    public class UserLocationController : ApiController
    {
        [HttpPost]
        public IHttpActionResult GetHistory([FromBody] LocationId location)
        {
            List<UserLocation> usersOnLocation = new List<UserLocation>();

            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT kor_ime FROM korisnici WHERE kor_lokacija = @location";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {

                    cmd.Parameters.Add("@location", System.Data.SqlDbType.Int).Value = int.Parse(location.locationId.ToString());

                    using (SqlDataReader data = cmd.ExecuteReader())
                    {

                        while (data.Read())
                        {
                            UserLocation usrLocation = new UserLocation();
                            usrLocation.usrName = data["kor_ime"].ToString();

                            usersOnLocation.Add(usrLocation);
                        }
                        if (usersOnLocation.Count() > 0)
                        {
                            return Ok(usersOnLocation);
                        }
                        else
                        {
                            return NotFound();
                        }
                    }
                }
            }
        }
    }
}
