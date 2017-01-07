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
    public class HistoryController : ApiController
    {
        [HttpPost]
        public IHttpActionResult GetHistory([FromBody] UsrId userIdRequest)
        {
            List<UserMovement> movementHistory = new List<UserMovement>();
            
            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT kor_id, pov_vrijeme, lok_naziv, lok_opis, kor_ime FROM povijest_kretanja LEFT JOIN korisnici ON pov_kor = kor_id LEFT JOIN lokacije ON lok_id = pov_loc WHERE kor_id = @usrId";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {

                    cmd.Parameters.Add("@usrId", System.Data.SqlDbType.VarChar).Value = userIdRequest.UserId;

                    using (SqlDataReader data = cmd.ExecuteReader())
                    {
                        
                        while (data.Read())
                        {
                            UserMovement moveme = new UserMovement();
                            moveme.time = data["pov_vrijeme"].ToString();
                            moveme.name = data["lok_naziv"].ToString();
                            moveme.description = data["lok_opis"].ToString();
                            moveme.user = data["kor_ime"].ToString();
                            movementHistory.Add(moveme);
                        }
                        if (movementHistory.Count() > 0)
                        {
                            return Ok(movementHistory);
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
