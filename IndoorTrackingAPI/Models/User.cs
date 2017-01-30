using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace IndoorTracking.Models
{
    public class User
    {
        public int userId { get; set; }
        public string userName { get; set; }
        public string passWord { get; set; }
        public string name { get; set; }
        public int locationId { get; set; }
        public string locationName { get; set; }
        public string locationCategory { get;  set; }
        public int currentLocationId { get; set; } 
        public string currentLocationName { get;  set; }
        public string currentLocationCategory { get;  set; }
        public int notification { get;  set; }

    }
}