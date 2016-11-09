using IndoorTracking.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace IndoorTracking.Controllers
{
    public class UserController : ApiController
    {
        Users[] user = new Users[] {
            new Users {Id= 1, userName="test", passWord="test", name="pero"},
            new Users {Id= 2, userName="test2", passWord="test2", name="pero2"}
        };
        public IEnumerable<Users> GetAllUsers()
        {
            return user;
        }
        public IHttpActionResult GetUser(int id)
        {
            var username = user.FirstOrDefault((p) => p.Id == id);
            
            if (username == null)
            {
                return NotFound();
            }
            return Ok(user);
        }
        
    }
}
