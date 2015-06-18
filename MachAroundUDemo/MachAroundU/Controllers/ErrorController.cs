using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MachAroundU.Controllers
{
    public class ErrorController : Controller
    {
        //
        // GET: /Default1/

        public ActionResult Oops()
        {
            return View();
        }

        public ActionResult NotFound()
        {
            return View();
        }
        
        public ActionResult InternalServerError()
        {
            return View();
        }


    }
}
