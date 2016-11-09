using IndoorTracking.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace IndoorTracking.Controllers
{
    public class LocationController : ApiController
    {
        Locations[] locations = new Locations[]
        {
            new Locations {Id=1,name="soba",macAddress="bel" }
        };

        public IEnumerable<Locations> GetAllLocations()
        {
            return locations;
        }

        public IHttpActionResult GetLocation(int id)
        {
            var location = locations.FirstOrDefault((p)=>p.Id == id);
            if(location == null)
            {
                return NotFound();
            }
            return Ok(location);
        }

        
    }
}
