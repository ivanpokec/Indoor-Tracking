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
    //[Authorize]
    [RoutePrefix("api/History")]
    public class HistoryController : ApiController
    {
        
        [HttpPost]
        [Route("GetAllHistoryForUser")]
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

        
        [HttpPost]
        [Route("GetDateForUser")]
        public IHttpActionResult GetUserDates([FromBody] UsrId userIdRequest)
        {
            List<Date> listOfDates = new List<Date>();

            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT DISTINCT CAST( pov_vrijeme AS DATE) AS pov_vrijeme FROM povijest_kretanja WHERE pov_kor = @usrId ORDER BY pov_vrijeme DESC ";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {

                    cmd.Parameters.Add("@usrId", System.Data.SqlDbType.VarChar).Value = userIdRequest.UserId;

                    using (SqlDataReader data = cmd.ExecuteReader())
                    {

                        while (data.Read())
                        {
                            Date newDate = new Date();
                            newDate.date = data["pov_vrijeme"].ToString();

                            listOfDates.Add(newDate);
                        }
                        if (listOfDates.Count() > 0)
                        {
                            return Ok(listOfDates);
                        }
                        else
                        {
                            return NotFound();
                        }
                    }
                }
            }
        }

        [HttpPost]
        [Route("GetHistoryForDateAndUser")]
        public IHttpActionResult GetTimeForDateAndUser([FromBody] UserDate userAndDate)
        {
            List<TimeLocation> timeLocationList = new List<TimeLocation>();

            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT CAST( pov_vrijeme AS DATE) AS pov_datum, CAST( pov_vrijeme AS TIME) AS pov_vrijeme, lok_naziv 
                                    FROM povijest_kretanja 
                                    LEFT JOIN lokacije ON lok_id = pov_loc 
                                    WHERE pov_kor = @usrId AND CAST( pov_vrijeme AS DATE) = dbo.date2(@date+'000000') ORDER BY pov_vrijeme DESC";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {

                    cmd.Parameters.Add("@usrId", System.Data.SqlDbType.Int).Value = userAndDate.UserId;
                    cmd.Parameters.Add("@date", System.Data.SqlDbType.VarChar).Value = userAndDate.date;

                    using (SqlDataReader data = cmd.ExecuteReader())
                    {

                        while (data.Read())
                        {
                            TimeLocation newData = new TimeLocation();
                            newData.date = data["pov_datum"].ToString();
                            newData.time = data["pov_vrijeme"].ToString();
                            newData.location = data["lok_naziv"].ToString();

                            timeLocationList.Add(newData);
                        }
                        if (timeLocationList.Count() > 0)
                        {
                            return Ok(timeLocationList);
                        }
                        else
                        {
                            return NotFound();
                        }
                    }
                }
            }
        }

        [HttpPost]
        [Route("GetHistoryFromTo")]
        public IHttpActionResult GetHistoryFromTo([FromBody] UserDateFromTo userFromTo)
        {
            List<TimeLocation> timeLocationList = new List<TimeLocation>();

            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT  CAST( pov_vrijeme AS DATE) AS pov_datum,CAST( pov_vrijeme AS TIME) AS pov_vrijeme, lok_naziv 
                                    FROM povijest_kretanja LEFT JOIN lokacije ON lok_id = pov_loc 
                                    WHERE pov_kor = @usrId AND CAST( pov_vrijeme AS DATE) >= dbo.date2(@dateFrom+'000000') AND CAST( pov_vrijeme AS DATE) <= dbo.date2(@dateTo+'000000') ORDER BY pov_datum DESC, pov_vrijeme DESC";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {

                    cmd.Parameters.Add("@usrId", System.Data.SqlDbType.Int).Value = userFromTo.UserId;
                    cmd.Parameters.Add("@dateFrom", System.Data.SqlDbType.VarChar).Value = userFromTo.dateFrom;
                    cmd.Parameters.Add("@dateTo", System.Data.SqlDbType.VarChar).Value = userFromTo.dateTo;

                    using (SqlDataReader data = cmd.ExecuteReader())
                    {

                        while (data.Read())
                        {
                            TimeLocation newData = new TimeLocation();
                            newData.date = data["pov_datum"].ToString();
                            newData.time = data["pov_vrijeme"].ToString();
                            newData.location = data["lok_naziv"].ToString();

                            timeLocationList.Add(newData);
                        }
                        if (timeLocationList.Count() > 0)
                        {
                            return Ok(timeLocationList);
                        }
                        else
                        {
                            return NotFound();
                        }
                    }
                }
            }
        }

        [HttpPost]
        [Route("GetHistoryForUserLocation")]
        public IHttpActionResult GetHistoryForUserLocation([FromBody] UserAndLocation userLocation)
        {
            List<Models.DateTime> dateTimeList = new List<Models.DateTime>();

            using (SqlConnection connection = new SqlConnection(Server.ConnectionString))
            {
                connection.Open();
                string strCmd = @"SELECT  CAST( pov_vrijeme AS DATE) AS pov_datum,CAST( pov_vrijeme AS TIME) AS pov_vrijeme 
                                    FROM povijest_kretanja 
                                    WHERE pov_kor = 1 AND pov_loc = 2 ORDER BY pov_datum DESC";
                using (SqlCommand cmd = new SqlCommand(strCmd, connection))
                {

                    cmd.Parameters.Add("@usrId", System.Data.SqlDbType.Int).Value = userLocation.UserId;
                    cmd.Parameters.Add("@location", System.Data.SqlDbType.VarChar).Value = userLocation.locationId;
                    

                    using (SqlDataReader data = cmd.ExecuteReader())
                    {

                        while (data.Read())
                        {
                            Models.DateTime newData = new Models.DateTime();
                            newData.date = data["pov_datum"].ToString();
                            newData.time = data["pov_vrijeme"].ToString();
                            

                            dateTimeList.Add(newData);
                        }
                        if (dateTimeList.Count() > 0)
                        {
                            return Ok(dateTimeList);
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
