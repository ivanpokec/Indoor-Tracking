using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace IndoorTracking.Models
{
    public class User
    {
        public int Id { get; set; }
        public string userName { get; set; }
        public string passWord { get; set; }
        public string name { get; set; }
        public int locationId { get; set; }
        public string locationName { get; set; }
    }
}